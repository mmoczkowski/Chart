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
import kotlin.math.pow

internal fun shiftFocus(
    currentFocus: LatLng,
    zoom: Int,
    offsetDelta: Offset,
    tileSize: Int
): LatLng {
    val offsetPx: Offset = currentFocus.getPixelCoords(zoom) + offsetDelta
    val tilesCount: Float = 2f.pow(zoom)
    val totalMapSize: Float = tilesCount * tileSize
    val loopedOffset = Offset(
        x = if (offsetPx.x < 0) {
            (totalMapSize * 2f - offsetPx.x)
        } else {
            offsetPx.x
        } % (totalMapSize * 2),
        y = if (offsetPx.y < 0) {
            (totalMapSize - offsetPx.y)
        } else {
            offsetPx.y
        } % totalMapSize
    )
    return loopedOffset.getLatLng(zoom)
}
