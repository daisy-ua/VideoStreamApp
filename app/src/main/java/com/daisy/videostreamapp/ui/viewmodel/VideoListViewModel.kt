package com.daisy.videostreamapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.domain.repository.VideoRepository
import com.daisy.videostreamapp.util.PlayerController
import com.daisy.videostreamapp.util.Resource
import com.daisy.videostreamapp.util.VideoStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    savedStateRepository: VideoStateRepository,
    private val videoRepository: VideoRepository,
    private val playerController: PlayerController
) : ViewModel() {
    val currentPlayer: Player
        get() = playerController.currentPlayer

    private val _videos: MutableStateFlow<Resource<List<VideoItem>>> =
        MutableStateFlow(Resource.Loading())
    val videos: StateFlow<Resource<List<VideoItem>>> = _videos
        .onStart {
            refreshData()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Resource.Loading())

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

    fun refreshData() = viewModelScope.launch {
        videoRepository.getVideos().collect {
            _videos.value = it
        }
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
        videos.collect { resource ->
            if (resource is Resource.Success) {
                playerController.initializePlaylist(resource.data!!, lastPlayedIndex.value)
            }
        }
    }

    private fun updateCurrentVideo(mediaItem: MediaItem) {
        val videoItem = videos.value.data?.find { it.mediaItem.first() == mediaItem }
        _currentVideo.value = videoItem
    }

    override fun onCleared() {
        super.onCleared()
        playerController.releasePlayer()
    }
}