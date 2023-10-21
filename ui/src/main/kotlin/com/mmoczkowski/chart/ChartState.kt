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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import com.mmoczkowski.chart.provider.api.Tile
import com.mmoczkowski.chart.provider.api.TileCoords
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.tan

class ChartState(
    val tileSize: Int,
    initialZoom: Int,
    initialFocus: LatLng,
    private val tilesRowCount: (Int) -> Int,
    private val tilesColCount: (Int) -> Int
) {
    private var zoom: Int by mutableStateOf(initialZoom)

    var focus: LatLng by mutableStateOf(initialFocus)
        internal set

    internal var viewportSize: IntSize by mutableStateOf(IntSize.Zero)

    private val tilesCount: IntSize by derivedStateOf {
        IntSize(
            width = tilesRowCount(zoom),
            height = tilesColCount(zoom)
        )
    }

    private val chartSize: IntSize by derivedStateOf {
        IntSize(
            width = tilesCount.width * tileSize,
            height = tilesCount.height * tileSize
        )
    }

    private val rawOffset: IntOffset by derivedStateOf {
        getPixelCoords(focus)
    }

    private val boundedOffset: IntOffset by derivedStateOf {
        val x = rawOffset.x - (viewportSize.width / 2)
        IntOffset(
            x = if (x < 0) {
                chartSize.width + (x % chartSize.width)
            } else {
                x
            }.toInt() % chartSize.width,
            y = rawOffset.y - viewportSize.height / 2
        )
    }

    private val viewport: IntRect by derivedStateOf {
        IntRect(
            topLeft = boundedOffset,
            bottomRight = IntOffset(
                x = boundedOffset.x + viewportSize.width + tileSize,
                y = min(
                    boundedOffset.y + viewportSize.height + tileSize,
                    chartSize.height
                )
            )
        )
    }

    val translationOffset: Offset by derivedStateOf {
        Offset(
            x = -((viewportSize.width - tileSize) / 2f) - boundedOffset.x,
            y = -((viewportSize.height - tileSize) / 2f) - boundedOffset.y
        )
    }

    val visibleTiles: List<Tile> by derivedStateOf {
        val firstVisibleX: Int = viewport.left / tileSize
        val lastVisibleX: Int = firstVisibleX + (viewport.width / tileSize)

        (firstVisibleX..lastVisibleX).flatMap { x ->
            val firstVisibleY: Int = max(
                0,
                viewport.top / tileSize
            )
            val lastVisibleY: Int = min(
                firstVisibleY + (viewport.height / tileSize),
                tilesCount.height - 1
            )

            (firstVisibleY..lastVisibleY).map { y ->
                Tile(
                    coords = TileCoords(
                        x = x % tilesCount.width,
                        y = y % tilesCount.height,
                        zoom = zoom
                    ),
                    pixelPosition = IntOffset(x = x * tileSize, y = y * tileSize)
                )
            }
        }
    }

    private fun setZoomLevel(newZoom: Int) {
        zoom = newZoom.coerceIn(0..MAX_ZOOM_LEVEL)
    }

    fun zoomIn() {
        setZoomLevel(zoom + 1)
    }

    fun zoomOut() {
        setZoomLevel(zoom - 1)
    }

    fun shiftFocus(offsetDelta: Offset) {
        focus = Offset(
            x = rawOffset.x - offsetDelta.x,
            y = (rawOffset.y - offsetDelta.y).coerceIn(
                minimumValue = 0f,
                maximumValue = (chartSize.height - 1).toFloat()
            )
        ).getLatLng()
    }

    private fun Offset.getLatLng(): LatLng {
        val worldX = (x / chartSize.width * (2 * PI) - PI)
        val worldY = PI - (y / chartSize.height * (2 * PI))

        val lat = Math.toDegrees(2 * atan(exp(worldY)) - PI / 2)
        val lon = Math.toDegrees(worldX)
        return LatLng(
            lat.toFloat(),
            lon.toFloat()
        )
    }

    fun getPixelCoords(position: LatLng): IntOffset {
        val latRad = Math.toRadians(position.latitude.toDouble()).toFloat()
        val lonRad = Math.toRadians(position.longitude.toDouble()).toFloat()
        val worldX: Float = tileSize / (2 * PI.toFloat()) * (lonRad + PI.toFloat())
        val worldY: Float = tileSize / (2 * PI.toFloat()) * (PI.toFloat() - ln(tan(PI.toFloat() / 4 + latRad / 2)))
        return IntOffset(
            x = (worldX * tilesCount.width).toInt(),
            y = (worldY * tilesCount.height).toInt(),
        )
    }

    companion object {
        private const val MAX_ZOOM_LEVEL: Int = 22
    }
}

@Composable
fun rememberChartState(
    tileSize: Int = 256,
    zoom: Int = 0,
    focus: LatLng = LatLng.NullIsland,
    tilesRowCount: (Int) -> Int = { z -> 2f.pow(z).toInt() },
    tilesColCount: (Int) -> Int = { z -> 2f.pow(z).toInt() }
): ChartState = remember(zoom, focus) {
    ChartState(
        tileSize = tileSize,
        initialZoom = zoom,
        initialFocus = focus,
        tilesRowCount = tilesRowCount,
        tilesColCount = tilesColCount
    )
}
