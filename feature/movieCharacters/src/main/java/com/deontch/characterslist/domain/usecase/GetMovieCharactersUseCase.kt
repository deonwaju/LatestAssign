package com.deontch.characterslist.domain.usecase

import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.models.MovieCharacterDomainModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the list of all movie characters.
 * It retrieves data from the repository, which handles the logic of fetching
 * from the network or local cache.
 */
class GetMovieCharactersUseCase @Inject constructor(
    private val movieCharacterRepository: MovieCharacterRepository
) {
    operator fun invoke(): Flow<List<MovieCharacterDomainModel>> {
        return movieCharacterRepository.getMovieCharacters()
    }
}
