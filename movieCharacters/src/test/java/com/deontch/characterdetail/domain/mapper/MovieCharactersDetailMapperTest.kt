package com.deontch.characterdetail.domain.mapper

import com.deontch.models.MovieCharacterDomainModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MovieCharactersDetailMapperTest {

    private fun createMockDomainModel(
        name: String = "Daenerys Targaryen",
        gender: String? = "Female",
        culture: String? = "Valyrian",
        born: String? = "In 284 AC",
        died: String? = "In 305 AC",
        titles: List<String> = listOf("Queen of the Andals", "Khaleesi"),
        aliases: List<String> = listOf("Dany", "Mother of Dragons"),
        tvSeries: List<String> = listOf("Season 1", "Season 8"),
        playedBy: List<String> = listOf("Emilia Clarke")
    ): MovieCharacterDomainModel {
        return MovieCharacterDomainModel(
            name = name,
            gender = gender,
            culture = culture,
            born = born,
            died = died,
            titles = titles,
            aliases = aliases,
            tvSeries = tvSeries,
            playedBy = playedBy,
            id = 1,
            seasons = emptyList()
        )
    }

    @Test
    fun `toDetailUiModel WHEN domain model is complete THEN maps all fields correctly`() {
        val domainModel = createMockDomainModel()

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("Daenerys Targaryen", uiModel.name)
        assertEquals("Female", uiModel.gender)
        assertEquals("Valyrian", uiModel.culture)
        assertEquals("In 284 AC", uiModel.born)
        assertEquals("In 305 AC", uiModel.died)
        assertEquals("Queen of the Andals, Khaleesi", uiModel.titles)
        assertEquals("Dany, Mother of Dragons", uiModel.aliases)
        assertEquals("Season 1, Season 8", uiModel.tvSeries)
        assertEquals("Emilia Clarke", uiModel.playedBy)
    }

    @Test
    fun `toDetailUiModel WHEN name is blank THEN maps to 'Unknown Name'`() {
        val domainModel = createMockDomainModel(name = "  ")

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("Unknown Name", uiModel.name)
    }

    @Test
    fun `toDetailUiModel WHEN optional fields are null THEN provides correct fallbacks`() {
        val domainModel = createMockDomainModel(
            gender = null,
            culture = null,
            born = null,
            died = null
        )

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("Unknown", uiModel.gender)
        assertEquals("Unknown", uiModel.culture)
        assertEquals("Unknown date of birth", uiModel.born)
        assertEquals("Still alive or date of death is unknown", uiModel.died)
    }

    @Test
    fun `toDetailUiModel WHEN optional fields are blank THEN provides correct fallbacks`() {
        val domainModel = createMockDomainModel(
            gender = " ",
            culture = "",
            born = "  ",
            died = " "
        )

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("Unknown", uiModel.gender)
        assertEquals("Unknown", uiModel.culture)
        assertEquals("Unknown date of birth", uiModel.born)
        assertEquals("Still alive or date of death is unknown", uiModel.died)
    }

    @Test
    fun `toDetailUiModel WHEN list fields are empty THEN provides correct fallbacks`() {
        val domainModel = createMockDomainModel(
            titles = emptyList(),
            aliases = emptyList(),
            tvSeries = emptyList(),
            playedBy = emptyList()
        )

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("No titles", uiModel.titles)
        assertEquals("No known aliases", uiModel.aliases)
        assertEquals("Not appeared in any series", uiModel.tvSeries)
        assertEquals("Actor information not available", uiModel.playedBy)
    }

    @Test
    fun `toDetailUiModel WHEN list fields contain only a blank string THEN provides correct fallbacks`() {
        val domainModel = createMockDomainModel(
            titles = listOf(""),
            aliases = listOf("  ")
        )

        val uiModel = domainModel.toDetailUiModel()

        assertEquals("No titles", uiModel.titles)
        assertEquals("No known aliases", uiModel.aliases)
    }
}
