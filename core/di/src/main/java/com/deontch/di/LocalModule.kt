package com.deontch.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.deontch.localdata.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val SHARED_PREFS: String = "sharedPrefs"

    @Singleton
    @Provides
    fun provideApplicationDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideMovieCharactersDao(appDatabase: AppDatabase) = appDatabase.movieCharactersDao()
}
