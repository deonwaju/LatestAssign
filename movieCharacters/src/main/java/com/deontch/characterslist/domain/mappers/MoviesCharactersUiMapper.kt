package com.deontch.characterslist.domain.mappers

import com.deontch.common.toRoman
import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterslist.presentation.model.MovieCharacterUiModel

/**
 * Converts a list of domain models into a list of UI models.
 */
internal fun List<MovieCharacterDomainModel>.toUiModel(): List<MovieCharacterUiModel> {
    return this.map { it.toUiModel() }
}

/**
 * Converts a single domain model into a UI model, preparing the data for display.
 * This now formats seasons as Roman numerals.
 */
internal fun MovieCharacterDomainModel.toUiModel(): MovieCharacterUiModel {
    return MovieCharacterUiModel(
        id = id,
        name = name.ifBlank { "Unknown Name" },
        culture = culture?.ifBlank { "Unknown" } ?: "Unknown",
        deathInfo = died?.ifBlank { "Not Applicable" } ?: "Not Applicable",
        seasonsAppeared = seasons?.joinToString(", ") { it.toRoman() }
            ?.let { "Seasons: $it" }
            ?: "No Season Info"
    )
}
