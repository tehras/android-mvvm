package com.github.tehras.base.demo.data.breeddetails.remote

import com.github.tehras.base.demo.data.breeddetails.BreedDetailsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface BreedDetailsService {
    @GET("/api/breed/{breed}/images")
    fun fetchBreedImages(@Path("breed") breed: String): Single<BreedDetailsResponse>
}