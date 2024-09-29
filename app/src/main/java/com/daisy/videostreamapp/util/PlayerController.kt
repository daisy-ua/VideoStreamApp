package com.daisy.videostreamapp.util

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.daisy.videostreamapp.domain.entity.VideoItem
import javax.inject.Inject

class PlayerController @Inject constructor(
    private val player: Player,
    private val savedStateRepository: VideoStateRepository
) {
    val currentPlayer: Player
        get() = player

    private var videoTransitionListener: ((MediaItem) -> Unit)? = null

    private val mediaItemTransitionListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.let { item ->
                videoTransitionListener?.invoke(item)
            }
        }
    }

    private val mediaItemErrorListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            Log.e("VideoListViewModel", "Player encountered an error: ${error.message}")
        }
    }

    init {
        player.addListener(mediaItemTransitionListener)
        player.addListener(mediaItemErrorListener)
    }

    fun setVideoTransitionListener(listener: (MediaItem) -> Unit) {
        videoTransitionListener = listener
    }

    fun playMedia(index: Int) {
        if (player.playbackState == Player.STATE_READY) {
            resetPlayerState()
            player.seekTo(index, savedStateRepository.savedStateVideoPosition)
            player.playWhenReady = true
        }
    }

    fun playMedia(item: VideoItem, savedPosition: Long) {
        player.clearMediaItems()
        player.setMediaItem(item.mediaItem.first())
        player.seekTo(savedPosition)
        player.prepare()
    }

    fun initializePlaylist(videos: List<VideoItem>, lastPlayedIndex: Int) {
        videos.forEachIndexed { index, item ->
            if (index < lastPlayedIndex) {
                player.addMediaItem(index, item.mediaItem.first())
            } else if (index > lastPlayedIndex) {
                player.addMediaItem(item.mediaItem.first())
            }
        }
        player.prepare()
    }

    fun savePlayerState(currentVideo: VideoItem?) {
        savedStateRepository.savePlayerState(player, currentVideo)
    }

    fun releasePlayer() {
        player.removeListener(mediaItemTransitionListener)
        player.removeListener(mediaItemErrorListener)
        videoTransitionListener = null
        player.release()
    }

    private fun resetPlayerState() {
        savedStateRepository.resetPlayerState()
    }
}