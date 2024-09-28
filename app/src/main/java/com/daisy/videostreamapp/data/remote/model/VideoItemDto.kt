package com.daisy.videostreamapp.data.remote.model

data class VideoItemDto(
    val description: String?,

    val subtitle: String?,

    val sources: List<String>?,

    val thumb: String?,

    val title: String?,
)

data class Category(
    val name: String,

    val videos: List<VideoItemDto>
)

data class MediaResponse(
    val categories: List<Category>
) {
    val videos: List<VideoItemDto>
        get() = categories.flatMap { category -> category.videos }
}

