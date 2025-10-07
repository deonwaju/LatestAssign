package com.deontch.network.service

import com.deontch.network.model.MovieCharacterResponse
import retrofit2.http.GET

interface MovieCharacterApi {
    @GET("characters")
    suspend fun getCharacters(): List<MovieCharacterResponse>
}
