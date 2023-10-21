/*
 * Copyright (C) 2023 Miłosz Moczkowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mmoczkowski.chart.sample.basic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mmoczkowski.chart.Chart
import com.mmoczkowski.chart.ChartState
import com.mmoczkowski.chart.LatLng
import com.mmoczkowski.chart.cache.impl.lru.rememberLruCache
import com.mmoczkowski.chart.provider.impl.osm.rememberOpenStreetMapTileProvider
import com.mmoczkowski.chart.rememberChartState

fun main() = application {
    Window(
        title = "Chart: Basic Sample",
        state = rememberWindowState(size = DpSize(800.dp, 1024.dp)),
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme {
            Box {
                val state: ChartState = rememberChartState(
                    zoom = 10,
                    focus = LatLng(52.2297f, 21.0122f)
                )
                Chart(
                    state = state,
                    provider = rememberOpenStreetMapTileProvider(),
                    cache = rememberLruCache(maxSize = 256)
                )
                MapControls(
                    onZoomIn = state::zoomIn,
                    onZoomOut = state::zoomOut
                )
            }
        }
    }
}

@Composable
private fun BoxScope.MapControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
    ) {
        IconButton(
            onClick = onZoomIn
        ) {
            Icon(imageVector = Icons.Default.ZoomIn, contentDescription = null)
        }
        IconButton(
            onClick = onZoomOut
        ) {
            Icon(imageVector = Icons.Default.ZoomOut, contentDescription = null)
        }
    }
}
