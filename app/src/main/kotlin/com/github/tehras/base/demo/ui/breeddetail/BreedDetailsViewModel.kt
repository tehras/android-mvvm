package com.github.tehras.base.demo.ui.breeddetail

import com.github.tehras.base.arch.ObservableViewModel
import com.github.tehras.base.demo.data.breeddetails.BreedDetailsDataStore
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.dagger.scopes.FragmentScope
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@FragmentScope
class BreedDetailsViewModel @Inject constructor(
    private val breed: Breed,
    private val breedDetailsDataStore: BreedDetailsDataStore
) :
    ObservableViewModel<BreedDetailsState, BreedDetailsUiEvent>() {

    override fun onCreate() {
        super.onCreate()

        val imageUrlsObservable = breedDetailsDataStore
            .observeData(breed)
            .map { it.images }
            .subscribeOn(Schedulers.io())
            .startWith(listOf<String>())

        imageUrlsObservable
            .flatMapCompletable {
                if (it.isEmpty()) {
                    breedDetailsDataStore.refreshFromRemote(breed)
                } else {
                    Completable.complete()
                }
            }
            .subscribeOn(Schedulers.io())
            .subscribe()

        val titleObservable = Observable.just(breed.name)

        Observables
            .combineLatest(imageUrlsObservable, titleObservable)
            .map { (urls, title) ->
                BreedDetailsState(
                    imageUrls = urls,
                    title = title
                )
            }
            .subscribeUntilDestroyed()
    }
}

data class BreedDetailsState(
    val imageUrls: List<String> = listOf(),
    val title: String
)

sealed class BreedDetailsUiEvent