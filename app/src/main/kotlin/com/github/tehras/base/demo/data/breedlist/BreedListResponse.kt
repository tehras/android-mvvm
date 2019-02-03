package com.github.tehras.base.demo.data.breedlist

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedListResponse(
    val status: String,
    @Json(name = "message")
    val data: Map<String, List<String>>
)

//@JsonClass(generateAdapter = true)
//data class Data(
//    val dogBreeds: Map<String, List<String>>
//)