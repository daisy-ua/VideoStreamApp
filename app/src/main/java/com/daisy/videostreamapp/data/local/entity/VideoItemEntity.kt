package com.daisy.videostreamapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video")
data class VideoItemEntity(
    val id: Long,

    val description: String?,

    val subtitle: String,

    val sources: List<String>,

    val thumb: String?,

    @PrimaryKey
    val title: String,
)
