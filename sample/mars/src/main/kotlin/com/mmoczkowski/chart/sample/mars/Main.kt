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

package com.mmoczkowski.chart.sample.mars

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mmoczkowski.chart.Chart
import com.mmoczkowski.chart.LatLng
import com.mmoczkowski.chart.MarkerScope.Companion.relativeOffset
import com.mmoczkowski.chart.cache.impl.lru.rememberLruCache
import com.mmoczkowski.chart.provider.api.TileCoords
import com.mmoczkowski.chart.provider.impl.url.rememberUrlTileProvider
import com.mmoczkowski.chart.rememberChartState
import com.mmoczkowski.chart.sample.mars.data.PointOfInterest
import com.mmoczkowski.chart.sample.mars.data.mapLayers
import com.mmoczkowski.chart.sample.mars.data.pointsOfInterests
import kotlin.math.pow

fun main() = application {
    Window(
        title = "Chart: Mars Sample",
        state = rememberWindowState(size = DpSize(800.dp, 1024.dp)),
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme(colors = darkColors()) {
            val chartState = rememberChartState(
                zoom = 3,
                tilesRowCount = { zoom -> (2f.pow(zoom) * 2).toInt() }
            )
            var selectedLayer: String by remember { mutableStateOf(mapLayers.first().id) }
            val urlBuilder: (TileCoords, Int) -> String = remember(selectedLayer) {
                { tileCoords, _ ->
                    "https://api.nasa.gov/mars-wmts/catalog/$selectedLayer/1.0.0/default/default028mm/" +
                            "${tileCoords.zoom}/" +
                            "${tileCoords.y}/" +
                            "${tileCoords.x}.png"
                }
            }

            Chart(
                state = chartState,
                markers = remember { pointsOfInterests },
                marker = { poi -> PoiMarker(poi) },
                markerPosition = { poi -> poi.position },
                provider = rememberUrlTileProvider(
                    urlBuilder = urlBuilder
                ),
                cache = rememberLruCache(keys = arrayOf(urlBuilder))
            )

            MapControls(
                focus = chartState.focus,
                selectedLayer = selectedLayer,
                onLayerSelected = { layer -> selectedLayer = layer },
                onZoomIn = chartState::zoomIn,
                onZoomOut = chartState::zoomOut
            )
        }
    }
}

@Composable
private fun MapControls(
    modifier: Modifier = Modifier,
    focus: LatLng,
    selectedLayer: String,
    onLayerSelected: (String) -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface {
            Text(
                text = focus.toString(),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
        }

        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            mapLayers.forEach { layer ->
                LayerButton(
                    isSelected = layer.id == selectedLayer,
                    name = layer.name
                ) {
                    onLayerSelected(layer.id)
                }
            }
        }

        Surface(modifier = Modifier.align(Alignment.BottomEnd)) {
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
private fun LayerButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    name: String,
    onClick: () -> Unit,
) {
    Surface(
        color = if (isSelected) {
            MaterialTheme.colors.surface
        } else {
            MaterialTheme.colors.onSurface
        },
        modifier = modifier.selectable(selected = isSelected, onClick = onClick)
    ) {
        Text(name, modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PoiMarker(poi: PointOfInterest, modifier: Modifier = Modifier) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = poi.title,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        modifier = modifier
            .wrapContentSize()
            .relativeOffset(Offset(0f, -0.5f))
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            modifier = Modifier.requiredSize(32.dp)
        )
    }
}
