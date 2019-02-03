package com.github.tehras.base.demo.data.breedlist.remote

import com.github.tehras.base.demo.data.breedlist.BreedListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface BreedListService {
    @GET("/api/breeds/list/all")
    fun fetchBreedList(): Single<BreedListResponse>
}