package com.github.tehras.base.demo.ui.breeddetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.base.demo.R
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.base.demo.ui.breeddetail.adapter.BreedDetailsAdapter
import com.github.tehras.base.ext.context.calculateNumberOfColumns
import com.github.tehras.base.ext.fragment.withBundle
import com.github.tehras.base.glide.GlideApp
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.navigationClicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_dog_details.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BreedDetailsFragment : Fragment() {
    companion object {
        fun instance(breed: Breed) = BreedDetailsFragment().withBundle { putParcelable(ARG_BREED, breed) }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModel<BreedDetailsViewModel> { factory }
    private val createDisposable = CompositeDisposable()

    private val breedDetailsAdapter by lazy { BreedDetailsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<BreedDetailsComponentCreator>()
            .plusBreedDetailsComponent()
            .breed(breed)
            .build()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dog_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolbar()

        createDisposable += viewModel
            .observeState()
            .map { it.imageUrls }
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .subscribe(breedDetailsAdapter)

        createDisposable += viewModel
            .observeState()
            .map { it.title }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::populateToolbar)
    }

    private fun initRecyclerView() {
        breed_details_rv.run {
            adapter = breedDetailsAdapter
            layoutManager = GridLayoutManager(
                context,
                context.calculateNumberOfColumns(widthPx = context.resources.getDimensionPixelSize(R.dimen.dog_image_size))
            )
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun initToolbar() {
        createDisposable += breed_details_toolbar
            .navigationClicks()
            .take(1)
            .subscribe { activity?.onBackPressed() }
    }

    private fun populateToolbar(title: String) {
        breed_details_toolbar.title = title
    }
}

private const val ARG_BREED = "com.github.tehras.base.demo.ui.breeddetail.breed"

private val BreedDetailsFragment.breed: Breed
    get() {
        return arguments!!.getParcelable(ARG_BREED)!!
    }