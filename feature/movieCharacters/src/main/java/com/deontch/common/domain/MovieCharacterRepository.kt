package com.deontch.common.domain

import com.deontch.models.MovieCharacterDomainModel
import kotlinx.coroutines.flow.Flow

interface MovieCharacterRepository {
    fun getMovieCharacters(): Flow<List<MovieCharacterDomainModel>>

    fun searchMovieCharacters(query: String): Flow<List<MovieCharacterDomainModel>>

    fun getCharacterByName(name: String): Flow<MovieCharacterDomainModel>
}
