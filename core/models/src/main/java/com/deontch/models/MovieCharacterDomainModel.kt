package com.deontch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_character_feed")
data class MovieCharacterDomainModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gender: String?,
    val culture: String?,
    val born: String?,
    val died: String?,
    val titles: List<String> = emptyList(),
    val aliases: List<String> = emptyList(),
    val tvSeries: List<String> = emptyList(),
    val playedBy: List<String> = emptyList(),
    val seasons: List<Int>? = null,
)
