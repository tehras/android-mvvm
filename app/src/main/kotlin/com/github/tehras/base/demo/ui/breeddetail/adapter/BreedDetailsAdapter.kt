package com.github.tehras.base.demo.ui.breeddetail.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.base.demo.R
import com.github.tehras.base.ext.views.viewHolderFromParent
import com.github.tehras.base.glide.GlideApp
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_dog_details.*

class BreedDetailsAdapter : RecyclerView.Adapter<BreedDetailsViewHolder>(), Consumer<List<String>> {
    private val breedImages: MutableList<String> = mutableListOf()
    private val breedDataDisposable = CompositeDisposable()

    override fun accept(urls: List<String>) {
        breedDataDisposable += Single
            .fromCallable {
                val diff = DiffUtil.calculateDiff(BreedDetailsCallback(breedImages, urls))
                Pair(urls, diff)
            }
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribeBy { (incomingUrls, diff) ->
                breedImages.clear()
                breedImages.addAll(incomingUrls)

                diff.dispatchUpdatesTo(this)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BreedDetailsViewHolder(parent.viewHolderFromParent(R.layout.list_item_dog_details))

    override fun getItemCount(): Int = breedImages.size
    override fun onBindViewHolder(holder: BreedDetailsViewHolder, position: Int) {
        holder.bind(breedImages[position])
    }
}

class BreedDetailsCallback(private val oldUrls: List<String>, private val newUrls: List<String>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldUrls[oldItemPosition] == newUrls[newItemPosition]

    override fun getOldListSize(): Int = oldUrls.size
    override fun getNewListSize(): Int = newUrls.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldUrls[oldItemPosition] == newUrls[newItemPosition]
}

class BreedDetailsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {
    fun bind(url: String) {
        GlideApp
            .with(breed_details_image)
            .load(url)
            .centerCrop()
            .transform(RoundedCorners(containerView.context.resources.getDimensionPixelSize(R.dimen.dog_image_corners)))
            .into(breed_details_image)
    }
}