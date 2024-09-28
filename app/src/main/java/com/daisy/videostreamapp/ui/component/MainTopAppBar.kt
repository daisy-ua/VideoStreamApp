package com.daisy.videostreamapp.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.daisy.videostreamapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    titleId: Int = R.string.app_name,
    navigationAction: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = navigationAction,
        actions = actions,
    )
}