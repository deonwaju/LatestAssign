package com.deontch.common.data

import com.deontch.base.providers.DispatcherProvider
import com.deontch.common.Constants.NO_INTERNET
import com.deontch.localdata.dao.MovieCharactersDao
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.network.provider.NetworkStateProvider
import com.deontch.network.service.MovieCharacterApi
import com.deontch.common.domain.MovieCharacterRepository
import com.deontch.characterslist.domain.mappers.toEntity
import com.deontch.ui.extensions.safeReturnableOperation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class MovieCharacterRepositoryImpl @Inject constructor(
    private val movieCharacterApi: MovieCharacterApi,
    private val movieCharactersDao: MovieCharactersDao,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider,
) : MovieCharacterRepository {

    override fun getMovieCharacters(): Flow<List<MovieCharacterDomainModel>> = flow {
        val charactersList = safeReturnableOperation(
            operation = {
                if (!networkStateProvider.isConnected) {
                    movieCharactersDao.getAllCharacters().ifEmpty {
                        throw Exception(NO_INTERNET)
                    }
                } else {
                    val apiResponse = movieCharacterApi.getCharacters()

                    val domainModel = apiResponse.toEntity()

                    movieCharactersDao.deleteAllCharacters()
                    movieCharactersDao.insertCharacters(domainModel)

                    domainModel
                }
            },
            actionOnException = {
                val exception = if (!networkStateProvider.isConnected) {
                    Exception(NO_INTERNET)
                } else {
                    it!!
                }
                throw exception
            },
            exceptionMessage = "MOVIE_CHARACTERS_ERROR_LOG"
        )

        emit(charactersList ?: emptyList())

    }.flowOn(dispatcherProvider.io)

    override fun searchMovieCharacters(query: String): Flow<List<MovieCharacterDomainModel>> =
        flow {
            val searchResult = safeReturnableOperation(
                operation = {
                    movieCharactersDao.searchCharacters(query)
                },
                actionOnException = {
                    throw if (!networkStateProvider.isConnected) {
                        Exception(NO_INTERNET)
                    } else {
                        it!!
                    }
                },
                exceptionMessage = "SEARCH_CHARACTERS_ERROR_LOG"
            )

            emit(searchResult ?: emptyList())

        }.flowOn(dispatcherProvider.io)

    override fun getCharacterByName(name: String): Flow<MovieCharacterDomainModel> = flow {
        emit(movieCharactersDao.getCharacterByName(name))
    }.flowOn(dispatcherProvider.io)
}
