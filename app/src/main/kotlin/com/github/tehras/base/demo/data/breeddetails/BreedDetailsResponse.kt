package com.github.tehras.base.demo.data.breeddetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedDetailsResponse(
    val status: String,
    @Json(name = "message")
    val data: List<String> // List of URLs
)