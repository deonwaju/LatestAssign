package com.deontch.characterdetail.domain.mapper

import com.deontch.models.MovieCharacterDomainModel
import com.deontch.characterdetail.presentation.model.MovieCharacterDetailUiModel

/**
 * Converts a single domain model into a detail UI model, preparing the data for the detail screen.
 * It handles null or blank fields to provide user-friendly default values.
 */
internal fun MovieCharacterDomainModel.toDetailUiModel(): MovieCharacterDetailUiModel {

    // Helper function to format lists, providing a fallback for empty lists.
    fun formatList(items: List<String>, fallback: String = "N/A"): String {
        return if (items.isNotEmpty() && items.first().isNotBlank()) {
            items.joinToString(", ")
        } else {
            fallback
        }
    }

    // Helper function to format optional strings, providing a fallback for null/blank strings.
    fun formatField(field: String?, fallback: String = "Unknown"): String {
        return if (field.isNullOrBlank()) fallback else field
    }

    return MovieCharacterDetailUiModel(
        name = name.ifBlank { "Unknown Name" },
        gender = formatField(gender),
        culture = formatField(culture),
        born = formatField(born, "Unknown date of birth"),
        died = formatField(died, "Still alive or date of death is unknown"),
        titles = formatList(titles, "No titles"),
        aliases = formatList(aliases, "No known aliases"),
        tvSeries = formatList(tvSeries, "Not appeared in any series"),
        playedBy = formatList(playedBy, "Actor information not available")
    )
}
