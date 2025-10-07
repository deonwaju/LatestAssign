package com.deontch.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deontch.models.MovieCharacterDomainModel

@Dao
interface MovieCharactersDao {

    /**
     * Inserts a list of movie characters into the database.
     * If a character with the same primary key already exists, it will be replaced.
     * @param characters The list of characters to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<MovieCharacterDomainModel>)

    /**
     * Retrieves all movie characters from the database.
     * @return A list of all MovieCharacterDomainModel objects.
     */
    @Query("SELECT * FROM movie_character_feed")
    suspend fun getAllCharacters(): List<MovieCharacterDomainModel>

    /**
     * Searches for movie characters where the name or culture matches the query.
     * The '%' wildcards allow for partial matches (e.g., searching for "Stark" will find "Arya Stark").
     * @param query The search string to match against character names or cultures.
     * @return A list of matching characters.
     */
    @Query("SELECT * FROM movie_character_feed WHERE name LIKE '%' || :query || '%' OR culture LIKE '%' || :query || '%'")
    suspend fun searchCharacters(query: String): List<MovieCharacterDomainModel>

    /**
     * Retrieves a single movie character by its unique ID.
     * @param id The primary key of the character.
     * @return The matching MovieCharacterDomainModel, or null if not found.
     */
    @Query("SELECT * FROM movie_character_feed WHERE name = :name")
    suspend fun getCharacterByName(name: String): MovieCharacterDomainModel

    /**
     * Deletes all entries from the movie_character_feed table.
     * @return The number of rows deleted.
     */
    @Query("DELETE FROM movie_character_feed")
    suspend fun deleteAllCharacters(): Int
}
