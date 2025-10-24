package com.example.lab10.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse(
    @Json(name = "results") val results: List<LocationDto>
)

@JsonClass(generateAdapter = true)
data class LocationDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "dimension") val dimension: String
)