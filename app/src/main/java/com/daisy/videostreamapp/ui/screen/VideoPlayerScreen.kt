package com.daisy.videostreamapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.ui.component.BackIconButton
import com.daisy.videostreamapp.ui.component.MainTopAppBar
import com.daisy.videostreamapp.ui.viewmodel.VideoListViewModel

@Composable
fun VideoPlayerScreen(
    videoIndex: Int,
    viewModel: VideoListViewModel,
    onUpClicked: () -> Unit,
) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event

            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                viewModel.savePlayerState()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(videoIndex) {
        viewModel.playVideo(videoIndex)
    }

    val currentVideo by viewModel.currentVideo.collectAsState()

    VideoPlayerContent(
        videoItem = currentVideo,
        onUpClicked = {
            viewModel.updateLastPlayedIndex()
            onUpClicked()
        },
        mediaPlayer = viewModel.currentPlayer,
        onPlayerUpdate = {
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }

                Lifecycle.Event.ON_STOP -> {
                    it.onPause()
                    it.player?.pause()
                }

                else -> Unit
            }
        }
    )
}

@Composable
fun VideoPlayerContent(
    videoItem: VideoItem?,
    onUpClicked: () -> Unit,
    mediaPlayer: Player,
    onPlayerUpdate: (PlayerView) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopAppBar(
                navigationAction = {
                    BackIconButton(onUpClicked)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = mediaPlayer
                    }
                },
                update = onPlayerUpdate,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )

            videoItem?.let { item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = item.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(.4f),
                        modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                    )

                    item.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}