package com.deontch.characterdetail.domain.usecases

import com.deontch.models.MovieCharacterDomainModel
import com.deontch.common.domain.MovieCharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Use case to get a single movie character by their exact name.
 * It retrieves the specific character details from the repository.
 */
class GetMovieCharacterByNameUseCase @Inject constructor(
    private val movieCharacterRepository: MovieCharacterRepository
) {
    operator fun invoke(name: String): Flow<MovieCharacterDomainModel> = movieCharacterRepository.getCharacterByName(name)
}
