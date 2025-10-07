package com.deontch.characterslist.domain.mappers

import com.deontch.models.MovieCharacterDomainModel
import com.deontch.network.model.MovieCharacterResponse

internal fun List<MovieCharacterResponse>.toEntity(): List<MovieCharacterDomainModel> {
    return this.map { it.toEntity() }
}


/**
 * Converts a single movie character object from the API layer to the domain layer.
 */
internal fun MovieCharacterResponse.toEntity(): MovieCharacterDomainModel {
    return with(this) {
        MovieCharacterDomainModel(
            name = name.toString(),
            culture = culture,
            died = died,
            gender = gender,
            born = born,
            aliases = aliases,
            titles = titles,
            tvSeries = tvSeries,
            playedBy = playedBy,
            seasons = tvSeries.mapIndexedNotNull { index, s ->
                if (s.isNotBlank()) index + 1 else null
            },
        )
    }
}