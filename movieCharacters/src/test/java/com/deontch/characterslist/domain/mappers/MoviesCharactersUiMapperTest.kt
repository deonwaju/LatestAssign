package com.deontch.characterslist.domain.mappers

import com.deontch.models.MovieCharacterDomainModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MoviesCharactersUiMapperTest {

    private fun createMockDomainModel(
        id: Int = 1,
        name: String = "Tyrion Lannister",
        culture: String? = "Westerlands",
        died: String? = "",
        seasons: List<Int>? = listOf(1, 2, 3, 4, 5, 6, 7, 8)
    ): MovieCharacterDomainModel {
        return MovieCharacterDomainModel(
            id = id,
            name = name,
            culture = culture,
            died = died,
            seasons = seasons,
            // Other fields are not used by the ui mapper, so they will be left as default
            gender = "asds",
            born = "adas",
            aliases = emptyList(),
            titles = emptyList(),
            tvSeries = emptyList(),
            playedBy = emptyList()
        )
    }

    @Test
    fun `toUiModel() WHEN domain model is complete THEN maps all fields correctly`() {
        val domainModel = createMockDomainModel()

        val uiModel = domainModel.toUiModel()

        assertEquals(1, uiModel.id)
        assertEquals("Tyrion Lannister", uiModel.name)
        assertEquals("Westerlands", uiModel.culture)
        assertEquals("Not Applicable", uiModel.deathInfo)
        assertEquals("Seasons: I, II, III, IV, V, VI, VII, VIII", uiModel.seasonsAppeared)
    }

    @Test
    fun `toUiModel() WHEN name is blank THEN maps to 'Unknown Name'`() {
        val domainModel = createMockDomainModel(name = "  ")

        val uiModel = domainModel.toUiModel()

        assertEquals("Unknown Name", uiModel.name)
    }

    @Test
    fun `toUiModel() WHEN culture is null THEN maps to 'Unknown'`() {
        val domainModel = createMockDomainModel(culture = null)

        val uiModel = domainModel.toUiModel()

        assertEquals("Unknown", uiModel.culture)
    }

    @Test
    fun `toUiModel() WHEN culture is blank THEN maps to 'Unknown'`() {
        val domainModel = createMockDomainModel(culture = " ")

        val uiModel = domainModel.toUiModel()

        assertEquals("Unknown", uiModel.culture)
    }

    @Test
    fun `toUiModel() WHEN died has info THEN uses that info for deathInfo`() {
        val domainModel = createMockDomainModel(died = "In 305 AC")

        val uiModel = domainModel.toUiModel()

        assertEquals("In 305 AC", uiModel.deathInfo)
    }

    @Test
    fun `toUiModel() WHEN died is null THEN maps to 'Not Applicable'`() {
        val domainModel = createMockDomainModel(died = null)

        val uiModel = domainModel.toUiModel()

        assertEquals("Not Applicable", uiModel.deathInfo)
    }

    @Test
    fun `toUiModel() WHEN seasons is null THEN maps to 'No Season Info'`() {
        val domainModel = createMockDomainModel(seasons = null)

        val uiModel = domainModel.toUiModel()

        assertEquals("No Season Info", uiModel.seasonsAppeared)
    }

    @Test
    fun `toUiModel() WHEN seasons is empty THEN maps to 'No Season Info'`() {
        val domainModel = createMockDomainModel(seasons = emptyList())

        val uiModel = domainModel.toUiModel()

        assertEquals("No Season Info", uiModel.seasonsAppeared)
    }

    @Test
    fun `list toUiModel() WHEN given a list THEN maps all items correctly`() {
        val domainList = listOf(
            createMockDomainModel(id = 1, name = "Tyrion Lannister"),
            createMockDomainModel(id = 2, name = "Jaime Lannister", seasons = listOf(1, 2, 8))
        )

        val uiList = domainList.toUiModel()

        assertEquals(2, uiList.size)
        assertEquals("Tyrion Lannister", uiList[0].name)
        assertEquals("Jaime Lannister", uiList[1].name)
        assertEquals("Seasons: I, II, VIII", uiList[1].seasonsAppeared)
    }

    @Test
    fun `list toUiModel() WHEN given an empty list THEN returns an empty list`() {
        val emptyDomainList = emptyList<MovieCharacterDomainModel>()

        val uiList = emptyDomainList.toUiModel()

        assert(uiList.isEmpty())
    }
}
