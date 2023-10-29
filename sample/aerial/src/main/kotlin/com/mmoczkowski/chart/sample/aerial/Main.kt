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

package com.mmoczkowski.chart.sample.aerial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
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
import com.mmoczkowski.chart.provider.impl.url.rememberUrlTileProvider
import com.mmoczkowski.chart.rememberChartState

fun main() = application {
    Window(
        title = "Chart: Aerial Sample",
        state = rememberWindowState(size = DpSize(800.dp, 1024.dp)),
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme(colors = darkColors()) {
            val state: ChartState = rememberChartState(
                minZoom = 7,
                maxZoom = 12,
                focus = LatLng(52.2297f, 21.0122f)
            )

            Chart(
                state = state,
                layers = listOf(
                    rememberBaseLayer(),
                    rememberAeroLayer()
                ),
                cache = rememberLruCache()
            )

            MapControls(
                onZoomIn = state::zoomIn,
                onZoomOut = state::zoomOut
            )
        }
    }
}

@Composable
private fun MapControls(
    modifier: Modifier = Modifier,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(modifier = Modifier.align(Alignment.BottomEnd).wrapContentSize()) {
            Column(
                modifier = Modifier.padding(8.dp)
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
    }
}

@Composable
private fun rememberAeroLayer() = rememberUrlTileProvider { tileCoords, _ ->
    "https://nwy-tiles-api.prod.newaydata.com/tiles/" +
            "${tileCoords.zoom}/" +
            "${tileCoords.x}/" +
            "${tileCoords.y}.png?path=2310/aero/latest"
}

@Composable
private fun rememberBaseLayer() = rememberUrlTileProvider { tileCoords, _ ->
    "https://nwy-tiles-api.prod.newaydata.com/tiles/" +
            "${tileCoords.zoom}/" +
            "${tileCoords.x}/" +
            "${tileCoords.y}.jpg?path=2310/base/latest"
}
