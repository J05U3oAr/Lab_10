package com.example.lab10.network

import com.example.lab10.network.dto.CharacterResponse
import com.example.lab10.network.dto.LocationResponse
import retrofit2.http.GET

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(): CharacterResponse

    @GET("location")
    suspend fun getLocations(): LocationResponse
}