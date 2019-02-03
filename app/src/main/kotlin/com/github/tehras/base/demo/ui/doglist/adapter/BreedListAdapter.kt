package com.github.tehras.base.demo.ui.doglist.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.tehras.base.demo.R
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.base.demo.ui.doglist.BreedListUiEvent
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_dog_breed.*
import java.util.concurrent.TimeUnit

class BreedListAdapter(private val clickConsumer: Consumer<BreedListUiEvent>) :
    RecyclerView.Adapter<BreedListViewHolder>(),
    Consumer<List<Breed>> {
    private val breeds = mutableListOf<Breed>()
    private val incomingDataDisposable = CompositeDisposable()

    override fun accept(newBreeds: List<Breed>) {
        incomingDataDisposable.clear()
        incomingDataDisposable += Single
            .fromCallable {
                val diff = DiffUtil.calculateDiff(BreedListDiffCallback(breeds, newBreeds))
                Pair(newBreeds, diff)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { (incomingBreeds, diff) ->
                breeds.clear()
                breeds.addAll(incomingBreeds)

                diff.dispatchUpdatesTo(this)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedListViewHolder =
        BreedListViewHolder(parent.viewHolderFromParent(R.layout.list_item_dog_breed), clickConsumer)

    override fun getItemCount(): Int = breeds.size
    override fun onBindViewHolder(holder: BreedListViewHolder, position: Int) {
        holder.bind(breeds[position])
    }
}

class BreedListDiffCallback(private val old: List<Breed>, private val new: List<Breed>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition].key == new[newItemPosition].key

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        old[oldItemPosition] == new[newItemPosition]

    override fun getOldListSize(): Int = old.size
    override fun getNewListSize(): Int = new.size
}

class BreedListViewHolder(
    override val containerView: View,
    private val clickConsumer: Consumer<BreedListUiEvent>
) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    private val clickDisposable = CompositeDisposable()

    fun bind(breed: Breed) {
        breed_name.text = breed.name

        clickDisposable.clear()
        clickDisposable += breed_clickable_layout
            .clicks()
            .map { BreedListUiEvent.BreedClicked(breed) }
            .throttleFirst(200, TimeUnit.MINUTES) // Don't allow overclicking
            .subscribe(clickConsumer)
    }
}