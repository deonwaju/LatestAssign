package com.deontch.network

import com.deontch.network.model.MovieCharacterResponse
import com.deontch.network.service.MovieCharacterApi
import io.mockk.coEvery
import io.mockk.mockk

class MockMovieCharacterApi {
    val api: MovieCharacterApi = mockk()

    fun setupMockMovieCharacterApi(response: List<MovieCharacterResponse>) {
        coEvery { api.getCharacters() } returns response
    }
}