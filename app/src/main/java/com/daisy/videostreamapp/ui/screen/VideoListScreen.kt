package com.daisy.videostreamapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.daisy.videostreamapp.ui.component.MainTopAppBar
import com.daisy.videostreamapp.ui.theme.VideoStreamAppTheme
import com.daisy.videostreamapp.ui.viewmodel.VideoListViewModel

@Composable
fun VideoListScreen(
    viewModel: VideoListViewModel,
    onItemClicked: (Int) -> Unit,
) {
    val videoList by viewModel.videos.collectAsState()
    val lastPlayedIndex by viewModel.lastPlayedIndex.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(lastPlayedIndex) {
        if (lastPlayedIndex != -1) {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            val isVisible = visibleItems.any { it.index == lastPlayedIndex }

            if (!isVisible) {
                listState.animateScrollToItem(lastPlayedIndex)
            }
        }
    }

    VideoListContent(
        videoList = videoList,
        listState = listState,
        onItemClicked = onItemClicked
    )
}

@Composable
fun VideoListContent(
    videoList: List<VideoItem>,
    listState: LazyListState,
    onItemClicked: (Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainTopAppBar() }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {

            itemsIndexed(
                items = videoList,
                key = { _, item -> item.title }
            ) { index, item ->
                VideoItemPreview(
                    video = item,
                    onItemClicked = { onItemClicked(index) }
                )
            }
        }
    }
}

@Composable
fun VideoItemPreview(
    video: VideoItem,
    onItemClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
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