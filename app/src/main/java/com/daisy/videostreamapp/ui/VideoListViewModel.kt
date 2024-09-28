package com.daisy.videostreamapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daisy.videostreamapp.data.repository.VideoRepository
import com.daisy.videostreamapp.domain.entity.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
) : ViewModel() {
    val videos: StateFlow<List<VideoItem>> = videoRepository.getVideos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}