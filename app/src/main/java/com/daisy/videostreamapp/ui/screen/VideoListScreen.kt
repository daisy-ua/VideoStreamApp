package com.daisy.videostreamapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.ui.VideoListViewModel
import com.daisy.videostreamapp.ui.component.MainTopAppBar
import com.daisy.videostreamapp.ui.theme.VideoStreamAppTheme

@Composable
fun VideoListScreen(
    viewModel: VideoListViewModel
) {
    val videoList by viewModel.videos.collectAsState()

    VideoListContent(
        videoList = videoList,
    )
}

@Composable
fun VideoListContent(
    videoList: List<VideoItem>,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainTopAppBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            items(
                items = videoList,
                key = { it.title }
            ) { item ->
                VideoItemPreview(
                    video = item
                )
            }
        }
    }
}

@Composable
fun VideoItemPreview(
    video: VideoItem,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 20.dp),
    ) {
        AsyncImage(
            model = video.fullThumbUrl,
            contentDescription = video.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            placeholder = ColorPainter(MaterialTheme.colorScheme.onSurface.copy(.4f)),
            error = ColorPainter(MaterialTheme.colorScheme.onSurface)
        )

        Text(
            text = video.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = video.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(.4f),
            modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoListContentPreview() {
    VideoStreamAppTheme {
//        VideoListContent()
    }
}