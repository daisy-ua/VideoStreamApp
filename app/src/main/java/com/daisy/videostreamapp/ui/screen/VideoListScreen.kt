package com.daisy.videostreamapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.daisy.videostreamapp.ui.component.MainTopAppBar
import com.daisy.videostreamapp.ui.theme.VideoStreamAppTheme

@Composable
fun VideoListScreen() {
    VideoListContent()
}

@Composable
fun VideoListContent() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainTopAppBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            items(5) {
                VideoItemPreview()
            }
        }
    }
}

@Composable
fun VideoItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
    ) {
        AsyncImage(
            model = "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            placeholder = ColorPainter(MaterialTheme.colorScheme.onSurface.copy(.4f)),
            error = ColorPainter(MaterialTheme.colorScheme.onSurface)
        )

        Text(
            text = "Video description",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VideoListContentPreview() {
    VideoStreamAppTheme {
        VideoListContent()
    }
}