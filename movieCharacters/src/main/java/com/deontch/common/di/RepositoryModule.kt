package com.deontch.common.di

import com.deontch.base.providers.DispatcherProvider
import com.deontch.common.data.MovieCharacterRepositoryImpl
import com.deontch.localdata.dao.MovieCharactersDao
import com.deontch.network.provider.NetworkStateProvider
import com.deontch.network.service.MovieCharacterApi
import com.deontch.common.domain.MovieCharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieCharacterRepository(
        characterApi: MovieCharacterApi,
        characterDao: MovieCharactersDao,
        dispatcherProvider: DispatcherProvider,
        networkStateProvider: NetworkStateProvider
    ): MovieCharacterRepository {
        return MovieCharacterRepositoryImpl(
            characterApi,
            characterDao,
            dispatcherProvider,
            networkStateProvider
        )
    }
}

