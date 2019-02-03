package com.github.tehras.base.demo.ui.doglist

import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.demo.data.breedlist.BreedListDataStore
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.dagger.scopes.FragmentScope
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

/**
 * @author tkoshkin
 */
@FragmentScope
class BreedListViewModel @Inject constructor(private val breedListDataStore: BreedListDataStore) :
    ObservableViewModel<DogListState, BreedListUiEvent>() {

    private val createDisposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()

        val searchObservable = uiEvents()
            .ofType<BreedListUiEvent.SearchInput>()
            .map { it.searchText.trim().toLowerCase() }
            .startWith("")

        val breedListData = breedListDataStore
            .observeData()
            .map { breeds ->
                breeds.sortedBy { it.name }
            }

        val breedClickObservable = uiEvents()
            .ofType<BreedListUiEvent.BreedClicked>()
            .map {
                DogListState.Screen.BreedDetails(it.breed)
            }
            .flatMap { Observable.just(it, DogListState.Screen.None) }
            .startWith(DogListState.Screen.None)

        val dogBreedObservable = Observables
            .combineLatest(breedListData, searchObservable)
            .map { (breeds, text) ->
                if (text.isEmpty()) {
                    breeds
                } else {
                    breeds
                        .filter { it.name.toLowerCase().contains(text) }
                }
            }

        createDisposables += breedListData
            .flatMapCompletable {
                if (it.isEmpty()) {
                    breedListDataStore.refreshFromRemote()
                } else {
                    Completable.complete()
                }
            }
            .subscribe()

        val showSearchObservable = uiEvents()
            .ofType<BreedListUiEvent.ToggleSearch>()
            .map { it.showSearch }
            .startWith(false)

        // Return state
        Observables
            .combineLatest(dogBreedObservable, breedClickObservable, showSearchObservable)
            .map { (breeds, screen, showSearch) ->
                DogListState(
                    breeds = breeds,
                    showScreen = screen,
                    showSearchFab = showSearch
                )
            }
            .subscribeUntilDestroyed()
    }

    override fun onDestroy() {
        createDisposables.clear()

        super.onDestroy()
    }
}

data class DogListState(
    val breeds: List<Breed>,
    val showScreen: Screen = Screen.None,
    val showSearchFab: Boolean = true
) {
    sealed class Screen {
        object None : Screen()
        data class BreedDetails(val breed: Breed) : Screen()
    }
}

sealed class BreedListUiEvent {
    data class BreedClicked(val breed: Breed) : BreedListUiEvent()
    data class SearchInput(val searchText: String) : BreedListUiEvent()
    data class ToggleSearch(val showSearch: Boolean) : BreedListUiEvent()
}
 