package com.github.tehras.base.demo.data.breeddetails

import com.github.tehras.base.demo.data.breeddetails.remote.BreedDetailsService
import com.github.tehras.base.demo.data.breedlist.remote.BreedListService
import com.github.tehras.base.demo.data.room.BreedDetailsDao
import com.github.tehras.base.demo.data.room.entities.Breed
import com.github.tehras.base.demo.data.room.entities.BreedDetails
import com.github.tehras.dagger.scopes.ApplicationScope
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@ApplicationScope
class BreedDetailsDataStore @Inject constructor(
    private val breedDetailsDao: BreedDetailsDao,
    private val breedDetailsService: BreedDetailsService
) {
    fun observeData(breed: Breed): Observable<BreedDetails> = breedDetailsDao.get(breed.key)

    fun refreshFromRemote(breed: Breed): Completable {
        return breedDetailsService
            .fetchBreedImages(breed.key)
            .map { convertToBreedDetails(breed.key, it) }
            .flatMapCompletable(breedDetailsDao::update)
    }

    private fun convertToBreedDetails(breedKey: String, breedListResponse: BreedDetailsResponse): BreedDetails {
        return BreedDetails(breedKey, breedListResponse.data)
    }
}