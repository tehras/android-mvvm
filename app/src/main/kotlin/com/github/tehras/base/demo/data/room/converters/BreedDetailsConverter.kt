package com.github.tehras.base.demo.data.room.converters

import androidx.room.TypeConverter

private const val SEPARATOR = "__,__"

class BreedDetailsConverter {
    @TypeConverter
    fun convertListToString(urls: List<String>): String {
        return urls.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun convertStringToList(urls: String): List<String> {
        return urls.split(SEPARATOR)
    }
}
