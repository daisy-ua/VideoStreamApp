package com.daisy.videostreamapp.domain.entity

import com.daisy.videostreamapp.data.remote.ApiPath

data class VideoItem(
    val id: Long,

    val description: String?,

    val subtitle: String,

    val sources: List<String>,

    val thumb: String?,

    val title: String,
) {
    val fullThumbUrl: String?
        get() = thumb?.let { "${ApiPath.IMAGE_BASE_URL}$it" }
}
