package com.deontch.characterslist.domain.mappers

import com.deontch.network.model.MovieCharacterResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviesCharacterRepositoryMapperTest {

    private fun createMockApiResponse(): MovieCharacterResponse {
        return MovieCharacterResponse(
            name = "Arya Stark",
            gender = "Female",
            culture = "Northmen",
            born = "In 289 AC",
            died = "",
            titles = arrayListOf("No One"),
            aliases = arrayListOf("Arya Horseface"),
            tvSeries = arrayListOf("Season 1", "Season 2", "Season 3", "Season 4", "", "Season 6", "Season 7", "Season 8"),
            playedBy = arrayListOf("Maisie Williams")
        )
    }

    @Test
    fun `toEntity() correctly maps all fields from response to domain model`() {
        val apiResponse = createMockApiResponse()

        val domainModel = apiResponse.toEntity()

        assertEquals("Arya Stark", domainModel.name)
        assertEquals("Female", domainModel.gender)
        assertEquals("Northmen", domainModel.culture)
        assertEquals("In 289 AC", domainModel.born)
        assertEquals("", domainModel.died)
        assertEquals(listOf("No One"), domainModel.titles)
        assertEquals(listOf("Arya Horseface"), domainModel.aliases)
        assertEquals(listOf("Maisie Williams"), domainModel.playedBy)
    }

    @Test
    fun `toEntity() correctly maps non-blank tvSeries to season numbers`() {
        val apiResponse = createMockApiResponse().apply {
            tvSeries = arrayListOf("Season 1", "Season 2", "Season 3", "Season 4", "", "Season 6", "Season 7", "Season 8")
        }
        val expectedSeasons = listOf(1, 2, 3, 4, 6, 7, 8) // Note: 5 is missing

        val domainModel = apiResponse.toEntity()

        assertEquals(expectedSeasons, domainModel.seasons)
        assertEquals(apiResponse.tvSeries, domainModel.tvSeries)
    }

    @Test
    fun `toEntity() handles empty tvSeries list gracefully`() {
        val apiResponse = createMockApiResponse().apply {
            tvSeries = arrayListOf()
        }

        val domainModel = apiResponse.toEntity()

        assertTrue("seasons list should be empty", domainModel.seasons?.isEmpty() == true)
        assertTrue("tvSeries list should be empty", domainModel.tvSeries.isEmpty())
    }

    @Test
    fun `toEntity() handles null properties from response`() {
        val apiResponseWithNulls = MovieCharacterResponse(
            name = null,
            gender = null,
            culture = null,
            born = null,
            died = null
        )

        val domainModel = apiResponseWithNulls.toEntity()

        assertEquals("null", domainModel.name)
        assertEquals(null, domainModel.gender)
        assertEquals(null, domainModel.culture)
        assertEquals(null, domainModel.born)
        assertEquals(null, domainModel.died)
        assertTrue(domainModel.titles.isEmpty())
        assertTrue(domainModel.aliases.isEmpty())
        assertTrue(domainModel.tvSeries.isEmpty())
        assertTrue(domainModel.playedBy.isEmpty())
        assertTrue(domainModel.seasons?.isEmpty() == true)
    }

    @Test
    fun `list_toEntity() correctly maps a list of responses to a list of domain models`() {
        val apiResponseList = listOf(
            createMockApiResponse(),
            createMockApiResponse().apply { name = "Sansa Stark" }
        )

        val domainModelList = apiResponseList.toEntity()

        assertEquals("List size should match", 2, domainModelList.size)
        assertEquals("First item name should be mapped correctly", "Arya Stark", domainModelList[0].name)
        assertEquals("Second item name should be mapped correctly", "Sansa Stark", domainModelList[1].name)
        assertEquals("First item seasons should be mapped", listOf(1, 2, 3, 4, 6, 7, 8), domainModelList[0].seasons)
    }

    @Test
    fun `list_toEntity() returns an empty list when given an empty list`() {
        val emptyApiResponseList = emptyList<MovieCharacterResponse>()

        val domainModelList = emptyApiResponseList.toEntity()

        assertTrue("The resulting list should be empty", domainModelList.isEmpty())
    }
}
