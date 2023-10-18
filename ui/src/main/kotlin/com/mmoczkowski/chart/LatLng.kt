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
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.tan

data class LatLng(
    val latitude: Float,
    val longitude: Float
) {
    fun getPixelCoords(zoom: Int): Offset {
        val latRad = Math.toRadians(latitude.toDouble()).toFloat()
        val lonRad = Math.toRadians(longitude.toDouble()).toFloat()
        val worldX: Float = 256 / (2 * PI.toFloat()) * (lonRad + PI.toFloat())
        val worldY: Float = 256 / (2 * PI.toFloat()) * (PI.toFloat() - ln(tan(PI.toFloat() / 4 + latRad / 2)))
        return Offset(
            x = -worldX * 2f.pow(zoom),
            y = -worldY * 2f.pow(zoom),
        )
    }

    override fun toString(): String {
        val latDirection = if (latitude >= 0) "N" else "S"
        val lonDirection = if (longitude >= 0) "E" else "W"

        return String.format(
            "%.3f° %s, %.3f° %s",
            abs(latitude), latDirection,
            abs(longitude), lonDirection
        )
    }

    companion object {
        val NullIsland = LatLng(latitude = 0f, longitude = 0f)
    }
}

fun Offset.getLatLng(zoom: Int): LatLng {
    val tiles = 2.0.pow(zoom)
    val tileSize = 256

    val worldX = (x / (tileSize * tiles) * (2 * PI) - PI)
    val worldY = PI - (y / (tileSize * tiles) * (2 * PI))

    val lat = Math.toDegrees(2 * atan(exp(worldY)) - PI / 2)
    val lon = Math.toDegrees(worldX)
    return LatLng(
        lat.toFloat(),
        lon.toFloat()
    )
}
