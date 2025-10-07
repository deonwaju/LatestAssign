package com.deontch.characterslist.presentation.model

import androidx.compose.runtime.Immutable

/**
 * A data class representing a single movie character, formatted for display in the UI.
 *
 * @param id A unique identifier for the character in the list (can be based on the database ID).
 * @param name The character's name.
 * @param culture The cultural affiliation of the character.
 * @param deathInfo Information about when or if the character died.
 * @param seasonsAppeared A formatted string listing the seasons the character appeared in.
 */
@Immutable
data class MovieCharacterUiModel(
    val id: Int,
    val name: String,
    val culture: String,
    val deathInfo: String,
    val seasonsAppeared: String
)
