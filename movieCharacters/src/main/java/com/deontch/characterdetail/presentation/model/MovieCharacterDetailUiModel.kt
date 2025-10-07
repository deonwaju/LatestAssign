package com.deontch.characterdetail.presentation.model

data class MovieCharacterDetailUiModel(
    val name: String,
    val gender: String,
    val culture: String,
    val born: String,
    val died: String,
    val titles: String,
    val aliases: String,
    val tvSeries: String,
    val playedBy: String
)
