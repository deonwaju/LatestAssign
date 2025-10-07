package com.deontch.di

import android.app.Application
import com.deontch.core.network.BuildConfig
import com.deontch.network.provider.NetworkStateProvider
import com.deontch.network.provider.NetworkStateProviderImpl
import com.deontch.network.service.MovieCharacterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val originalRequest: Request = chain.request()
                val requestWithAuth: Request = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.CHARACTERS_AUTH_TOKEN}")
                    .build()
                chain.proceed(requestWithAuth)
            }
            .build()
    }

    @Provides
    @Singleton
    fun providesCharactersRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.CHARACTERS_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideMovieCharacterApiService(retrofit: Retrofit): MovieCharacterApi {
        return retrofit.create(MovieCharacterApi::class.java)
    }

    @Provides
    fun provideNetworkMonitor(application: Application): NetworkStateProvider {
        return NetworkStateProviderImpl(application)
    }
}

private const val NETWORK_TIMEOUT = 30L
