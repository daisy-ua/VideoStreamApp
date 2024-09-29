package com.daisy.videostreamapp.util

import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.Player
import com.daisy.videostreamapp.domain.entity.VideoItem
import javax.inject.Inject

class VideoStateRepository @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) {

    val savedStateVideoItem: VideoItem?
        get() = savedStateHandle[VIDEO_ITEM_KEY]

    val savedStateVideoIndex: Int
        get() = savedStateHandle[VIDEO_INDEX_KEY] ?: -1

    val savedStateVideoPosition: Long
        get() = savedStateHandle[VIDEO_POSITION_KEY] ?: 0L

    fun savePlayerState(player: Player, currentVideo: VideoItem?) {
        savedStateHandle[VIDEO_INDEX_KEY] = player.currentMediaItemIndex
        savedStateHandle[VIDEO_POSITION_KEY] = player.currentPosition
        savedStateHandle[VIDEO_ITEM_KEY] = currentVideo
    }

    fun resetPlayerState() {
        savedStateHandle[VIDEO_INDEX_KEY] = null
        savedStateHandle[VIDEO_POSITION_KEY] = null
        savedStateHandle[VIDEO_ITEM_KEY] = null
    }

    companion object {
        private const val VIDEO_INDEX_KEY = "video_index"
        private const val VIDEO_POSITION_KEY = "video_position"
        private const val VIDEO_ITEM_KEY = "video_item"
    }
}