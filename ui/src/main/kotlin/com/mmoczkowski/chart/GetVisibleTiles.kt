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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.mmoczkowski.chart.provider.api.Tile
import com.mmoczkowski.chart.provider.api.TileCoords
import kotlin.math.ceil

internal fun getVisibleTiles(
    viewportSize: IntSize,
    totalTilesCount: IntSize,
    zoom: Int,
    tileSize: Int,
    offset: Offset,
): List<Tile> {
    val visibleTilesRowCount: Int = ceil(viewportSize.width / tileSize.toFloat()).toInt() + 1
    val visibleTilesColCount: Int = ceil(viewportSize.height / tileSize.toFloat()).toInt() + 1
    val viewportCenter = IntOffset(viewportSize.width / 2, viewportSize.height / 2)
    val centeredOffset = IntOffset(x = offset.x.toInt() + viewportCenter.x, y = offset.y.toInt() + viewportCenter.y)
    val tileRowOffset: IntOffset = (centeredOffset / tileSize) % totalTilesCount.width
    val tileColOffset: IntOffset = (centeredOffset / tileSize) % totalTilesCount.height
    val xIndexOfFirstVisibleTile: Int = totalTilesCount.width - tileRowOffset.x - 1
    val yIndexOfFirstVisibleTile: Int = totalTilesCount.height - tileColOffset.y - 1
    val relativeTileOffset: IntOffset = centeredOffset % tileSize

    return (0..visibleTilesRowCount).flatMap { x ->
        (0..visibleTilesColCount).map { y ->
            Tile(
                coords = TileCoords(
                    x = (x + xIndexOfFirstVisibleTile) % totalTilesCount.width,
                    y = (y + yIndexOfFirstVisibleTile) % totalTilesCount.height,
                    zoom = zoom,
                ),
                pixelPosition = IntOffset(
                    x = (x * tileSize + relativeTileOffset.x - tileSize / 2),
                    y = (y * tileSize + relativeTileOffset.y - tileSize / 2),
                )
            )
        }
    }
}

private operator fun IntOffset.div(scalar: Int): IntOffset = IntOffset(x / scalar, y / scalar)
