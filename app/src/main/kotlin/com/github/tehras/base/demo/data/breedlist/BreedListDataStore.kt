package com.github.tehras.base.demo.data.breedlist

import com.github.tehras.base.demo.data.breedlist.remote.BreedListService
import com.github.tehras.base.demo.data.room.BreedDao
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.dagger.scopes.ApplicationScope
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@ApplicationScope
class BreedListDataStore @Inject constructor(
    private val breedDao: BreedDao,
    private val breedListService: BreedListService
) {
    fun observeData(): Observable<List<Breed>> = breedDao.getAll()

    fun refreshFromRemote(): Completable {
        return breedListService
            .fetchBreedList()
            .map(::convertToBreeds)
            .flatMapCompletable(breedDao::update)
    }

    private fun convertToBreeds(breedListResponse: BreedListResponse): List<Breed> {
        val breeds = mutableListOf<Breed>()
        breedListResponse.data?.let { data ->
            data.forEach { entry ->
                if (entry.value.isEmpty()) {
                    breeds.add(Breed(entry.key, entry.key.capitalize()))
                } else {
                    entry.value.forEach { subBreed ->
                        breeds.add(
                            Breed(
                                key = "${entry.key}-$subBreed",
                                name = "${subBreed.capitalize()} ${entry.key.capitalize()}"
                            )
                        )
                    }
                }
            }
        }
        return breeds
    }
}