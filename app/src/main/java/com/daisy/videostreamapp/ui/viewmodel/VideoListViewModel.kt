package com.daisy.videostreamapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.domain.repository.VideoRepository
import com.daisy.videostreamapp.util.PlayerController
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
    savedStateRepository: VideoStateRepository,
    videoRepository: VideoRepository,
    private val playerController: PlayerController
) : ViewModel() {
    val currentPlayer: Player
        get() = playerController.currentPlayer

    val videos: StateFlow<List<VideoItem>> = videoRepository.getVideos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
    val currentVideo: StateFlow<VideoItem?> = _currentVideo

    private var _lastPlayedIndex = MutableStateFlow(-1)
    val lastPlayedIndex: StateFlow<Int> get() = _lastPlayedIndex

    init {
        _currentVideo.value = savedStateRepository.savedStateVideoItem
        val savedPosition = savedStateRepository.savedStateVideoPosition
        _lastPlayedIndex.value = savedStateRepository.savedStateVideoIndex

        currentVideo.value?.let {
            playerController.playMedia(it, savedPosition)
        }

        playerController.setVideoTransitionListener { mediaItem ->
            updateCurrentVideo(mediaItem)
        }

        initializePlaylist()
    }

    fun playVideo(index: Int) {
        playerController.playMedia(index)
    }

    fun updateLastPlayedIndex() {
        _lastPlayedIndex.value = playerController.currentPlayer.currentMediaItemIndex
    }

    fun savePlayerState() {
        playerController.savePlayerState(currentVideo.value)
    }

    private fun initializePlaylist() = viewModelScope.launch {
        videos.collect {
            playerController.initializePlaylist(it, lastPlayedIndex.value)
        }
    }

    private fun updateCurrentVideo(mediaItem: MediaItem) {
        val videoItem = videos.value.find { it.mediaItem.first() == mediaItem }
        _currentVideo.value = videoItem
    }

    override fun onCleared() {
        super.onCleared()
        playerController.releasePlayer()
    }
}