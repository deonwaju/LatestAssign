package com.deontch.localdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.deontch.localdata.dao.MovieCharactersDao
import com.deontch.localdata.typeconverter.Converters
import com.deontch.models.MovieCharacterDomainModel

const val DB_NAME = "movie_characters_database"

@Database(
    entities = [MovieCharacterDomainModel::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieCharactersDao(): MovieCharactersDao

    companion object {
        const val DATABASE_NAME = DB_NAME
    }
}
