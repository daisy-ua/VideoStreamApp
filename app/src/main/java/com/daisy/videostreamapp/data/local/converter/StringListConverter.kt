package com.daisy.videostreamapp.data.local.converter

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")
    }
}