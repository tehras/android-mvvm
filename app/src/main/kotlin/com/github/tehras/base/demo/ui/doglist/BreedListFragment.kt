package com.github.tehras.base.demo.ui.doglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.github.tehras.base.arch.viewModel
import com.github.tehras.base.dagger.components.findComponent
import com.github.tehras.base.demo.DemoActivity
import com.github.tehras.base.demo.R
import com.github.tehras.base.demo.ui.breeddetail.BreedDetailsFragment
import com.github.tehras.base.demo.ui.doglist.adapter.BreedListAdapter
import com.github.tehras.base.ext.context.calculateNumberOfColumns
import com.github.tehras.base.ext.context.hideKeyboard
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_dog_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author tkoshkin
 */
class BreedListFragment : Fragment() {
    companion object {
        fun instance() = BreedListFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModel<BreedListViewModel> { factory }
    private val createDisposable = CompositeDisposable()
    private val startDisposable = CompositeDisposable()

    private val breedAdapter by lazy { BreedListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findComponent<DogListComponentCreator>()
            .plusDogListComponent()
            .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dog_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolbar()

        createDisposable += viewModel
            .observeState()
            .map { it.breeds }
            .distinctUntilChanged()
            .subscribe(breedAdapter)

        createDisposable += viewModel
            .observeState()
            .map { it.showScreen }
            .filter { it != DogListState.Screen.None }
            .subscribe(::handleShowingNewScreen)

        createDisposable += viewModel
            .observeState()
            .map { it.showSearchFab }
            .distinctUntilChanged()
            .subscribe(::handleSearch)
    }

    override fun onStart() {
        super.onStart()

        startDisposable += dog_list_search_fab
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .map { BreedListUiEvent.ToggleSearch(dog_list_search_fab.visibility == View.VISIBLE) }
            .subscribe(viewModel)

        startDisposable += dog_list_search_layout
            .textChanges()
            .throttleLatest(100, TimeUnit.MILLISECONDS)
            .map { BreedListUiEvent.SearchInput(it.toString()) }
            .subscribe(viewModel)

        startDisposable += dog_list_search_layout
            .stopSearch()
            .map { BreedListUiEvent.ToggleSearch(false) }
            .subscribe(viewModel)
    }

    override fun onDestroyView() {
        createDisposable.clear()

        super.onDestroyView()
    }

    override fun onStop() {
        activity?.hideKeyboard()
        startDisposable.clear()

        super.onStop()
    }

    private fun handleShowingNewScreen(screen: DogListState.Screen) {
        when (screen) {
            is DogListState.Screen.BreedDetails -> {
                (activity as? DemoActivity)?.showFragment(BreedDetailsFragment.instance(screen.breed))
            }
        }
    }

    private fun handleSearch(showSearch: Boolean) {
        if (showSearch) {
            dog_list_search_layout.show()
            dog_list_search_fab.hide()
        } else {
            dog_list_search_layout.hide()
            dog_list_search_fab.show()
        }
    }

    private fun initRecyclerView() {
        dog_list_rv.run {
            adapter = breedAdapter
            layoutManager = GridLayoutManager(
                context,
                context.calculateNumberOfColumns()
            )
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun initToolbar() {
        createDisposable += dog_list_toolbar
            .navigationClicks()
            .subscribe { activity?.onBackPressed() }
    }
}