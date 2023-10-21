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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
internal fun <T> MarkerLayer(
    modifier: Modifier = Modifier,
    markers: List<T>,
    markerPosition: (T) -> LatLng,
    marker: @Composable MarkerScope.(T) -> Unit,
    state: ChartState,
) {
    Layout(
        content = {
            markers.forEach { item ->
                MarkerScope.marker(item)
            }
        },
        modifier = modifier.fillMaxSize()
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val item: T = markers[index]
                val position: LatLng = markerPosition(item)

                placeable.placeRelativeWithLayer(
                    position = state.getPixelCoords(position),
                    zIndex = 0f,
                ) {
                    translationX = state.translationOffset.x
                    translationY = state.translationOffset.y
                }
            }
        }
    }
}