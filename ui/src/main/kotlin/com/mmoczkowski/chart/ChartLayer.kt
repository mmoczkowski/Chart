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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import com.mmoczkowski.chart.cache.api.Cache
import com.mmoczkowski.chart.provider.api.Tile
import com.mmoczkowski.chart.provider.api.TileCoords
import com.mmoczkowski.chart.provider.api.TileProvider

@Composable
internal fun ChartLayer(
    modifier: Modifier = Modifier,
    tiles: List<Tile>,
    success: @Composable (TileCoords, ImageBitmap) -> Unit,
    error: @Composable () -> Unit,
    loading: @Composable () -> Unit,
    provider: TileProvider,
    cache: Cache<TileCoords, ImageBitmap>
) {
    Layout(
        content = {
            tiles.forEach { tile ->
                key(tile.coords) {
                    var tileState: TileState by remember { mutableStateOf(TileState.Loading) }
                    LaunchedEffect(tile.coords, provider) {
                        tileState = TileState.Loading
                        tileState = try {
                            val image: ImageBitmap = cache.get(tile.coords) { coords ->
                                provider.getTile(coords)
                            }
                            TileState.Success(tile.coords, image = image)
                        } catch (exception: Exception) {
                            TileState.Error
                        }
                    }
                    val sizePx = with(LocalDensity.current) { provider.tileSize.toDp() }

                    TileContent(
                        state = tileState,
                        success = success,
                        error = error,
                        loading = loading,
                        modifier = Modifier.requiredSize(sizePx, sizePx)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.mapIndexed { index, placeable ->
                tiles[index] to placeable
            }.forEach { (tile, placeable) ->
                placeable.place(tile.pixelPosition)
            }
        }
    }
}

@Composable
private fun TileContent(
    state: TileState,
    success: @Composable (TileCoords, ImageBitmap) -> Unit,
    error: @Composable () -> Unit,
    loading: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        when (state) {
            is TileState.Success -> success(state.coords, state.image)
            TileState.Error -> error()
            TileState.Loading -> loading()
        }
    }
}
