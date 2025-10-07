package com.deontch.localdata.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return value?.let { gson.fromJson(it, listType) }
    }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return value?.let { gson.fromJson(it, listType) }
    }

    @TypeConverter
    fun fromAny(value: Any?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAny(value: String?): Any? {
        return value?.let { gson.fromJson(it, Any::class.java) }
    }
}
