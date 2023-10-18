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

package com.mmoczkowski.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.pow

class ChartState(
    initialZoom: Int,
    initialFocus: LatLng,
    tilesRowCount: (Int) -> Int,
    tilesColCount: (Int) -> Int
) {
    var zoom: Int by mutableStateOf(initialZoom)
        private set

    var focus: LatLng by mutableStateOf(initialFocus)

    val offset: Offset by derivedStateOf { focus.getPixelCoords(zoom) }

    private fun setZoomLevel(newZoom: Int) {
        zoom = newZoom.coerceIn(0..MAX_ZOOM_LEVEL)
    }

    fun zoomIn() {
        setZoomLevel(zoom + 1)
    }

    fun zoomOut() {
        setZoomLevel(zoom - 1)
    }

    val totalTilesCount: IntSize by derivedStateOf {
        IntSize(
            width = tilesRowCount(zoom),
            height = tilesColCount(zoom)
        )
    }

    companion object {
        private const val MAX_ZOOM_LEVEL: Int = 22
    }
}

@Composable
fun rememberChartState(
    zoom: Int = 0,
    focus: LatLng = LatLng.NullIsland,
    tilesRowCount: (Int) -> Int = { z -> 2f.pow(z).toInt() },
    tilesColCount: (Int) -> Int = { z -> 2f.pow(z).toInt() }
): ChartState = remember(zoom, focus) {
    ChartState(
        initialZoom = zoom,
        initialFocus = focus,
        tilesRowCount = tilesRowCount,
        tilesColCount = tilesColCount
    )
}
