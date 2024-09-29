package com.daisy.videostreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.daisy.videostreamapp.data.repository.VideoRepository
import com.daisy.videostreamapp.domain.entity.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    videoRepository: VideoRepository,
    private val player: Player,
) : ViewModel() {
    val videos: StateFlow<List<VideoItem>> = videoRepository.getVideos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
    val currentVideo: StateFlow<VideoItem?> = _currentVideo

    val currentPlayer: Player
        get() = player

    private var _lastPlayedIndex = MutableStateFlow(-1)
    val lastPlayedIndex: StateFlow<Int> get() = _lastPlayedIndex

    private val mediaItemTransitionListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.let { item ->
                val videoItem = videos.value.find { it.mediaItem.first() == item }
                _currentVideo.value = videoItem
            }
        }
    }

    private val mediaItemErrorListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            Log.e("VideoListViewModel", "Player encountered an error: ${error.message}")
        }
    }

    init {
        initializePlaylist()
        player.addListener(mediaItemTransitionListener)
        player.addListener(mediaItemErrorListener)
    }

    fun playVideo(index: Int) {
        if (index < player.mediaItemCount) {
            player.seekTo(index, 0)
            player.playWhenReady = true
        }
    }

    fun updateLastPlayedIndex() {
        _lastPlayedIndex.value = player.currentMediaItemIndex
    }

    private fun initializePlaylist() = viewModelScope.launch {
        videos.collect {
            it.forEach { item ->
                player.addMediaItem(item.mediaItem.first())
            }

            player.prepare()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.apply {
            removeListener(mediaItemTransitionListener)
            removeListener(mediaItemErrorListener)
            release()
        }
    }
}

val videoUrl =
    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

val fakeUrl =
    "http://commondatastorage.gooiiigleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
