package com.deontch.localdata

import com.deontch.localdata.typeconverter.Converters
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ConvertersTest {

    private lateinit var converters: Converters

    @Before
    fun setUp() {
        converters = Converters()
    }

    @Test
    fun fromStringList_nonNullList_returnsJsonString() {
        val input = listOf("One", "Two", "Three")

        val result = converters.fromStringList(input)

        val expected = Gson().toJson(input)
        assertEquals(expected, result)
    }

    @Test
    fun fromStringList_nullList_returnsNull() {
        val result = converters.fromStringList(null)

        assertNull(result)
    }

    @Test
    fun toStringList_nonJsonString_returnsList() {
        val jsonString = """["One", "Two", "Three"]"""

        val result = converters.toStringList(jsonString)

        val expected = listOf("One", "Two", "Three")
        assertEquals(expected, result)
    }

    @Test
    fun toStringList_nullString_returnsNull() {
        val result = converters.toStringList(null)

        assertNull(result)
    }

    @Test
    fun fromAny_nonNullObject_returnsJsonString() {
        val input = SampleData("Test", 123)

        val result = converters.fromAny(input)

        val expected = Gson().toJson(input)
        assertEquals(expected, result)
    }

    @Test
    fun fromAny_nullObject_returnsNull() {
        val result = converters.fromAny(null)

        assertNull(result)
    }

    @Test
    fun toAny_nullString_returnsNull() {
        val result = converters.toAny(null)

        assertNull(result)
    }
}

data class SampleData(val name: String, val value: Int)
