package com.deontch.characterslist.domain.usecase

import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.models.MovieCharacterDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to search for movie characters based on a query.
 * It delegates the search operation to the repository.
 */
class SearchMovieCharactersUseCase @Inject constructor(
    private val movieCharacterRepository: MovieCharacterRepository
) {
    operator fun invoke(query: String): Flow<List<MovieCharacterDomainModel>> =
        movieCharacterRepository.searchMovieCharacters(query)
}
