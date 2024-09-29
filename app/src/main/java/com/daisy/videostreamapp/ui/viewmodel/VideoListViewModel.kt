package com.daisy.videostreamapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.daisy.videostreamapp.data.repository.VideoRepository
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.util.VideoStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val savedStateRepository: VideoStateRepository,
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
        _currentVideo.value = savedStateRepository.savedStateVideoItem
        val savedPosition = savedStateRepository.savedStateVideoPosition
        _lastPlayedIndex.value = savedStateRepository.savedStateVideoIndex

        currentVideo.value?.let {
            player.clearMediaItems()
            player.setMediaItem(it.mediaItem.first())
            player.seekTo(savedPosition)
            player.prepare()
        }

        initializePlaylist()
        player.addListener(mediaItemTransitionListener)
        player.addListener(mediaItemErrorListener)
    }

    fun savePlayerState() {
        savedStateRepository.savePlayerState(player, currentVideo.value)
    }

    private fun resetPlayerState() {
        savedStateRepository.resetPlayerState()
    }

    fun playVideo(index: Int) {
        if (player.playbackState == Player.STATE_READY) {
            resetPlayerState()
            player.seekTo(index, savedStateRepository.savedStateVideoPosition)
            player.playWhenReady = true
        }
    }

    fun updateLastPlayedIndex() {
        _lastPlayedIndex.value = player.currentMediaItemIndex
    }

    private fun initializePlaylist() = viewModelScope.launch {
        videos.collect {
            it.forEachIndexed { index, item ->
                if (index < lastPlayedIndex.value) {
                    player.addMediaItem(index, item.mediaItem.first())
                } else if (index > lastPlayedIndex.value) {
                    player.addMediaItem(item.mediaItem.first())
                }
            }
            Log.d("daisy-ua", "Player init: ${player.mediaItemCount}")
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