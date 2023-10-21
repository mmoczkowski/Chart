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

import kotlin.math.abs

class LatLng(
    latitude: Float,
    longitude: Float
) {
    val latitude: Float = latitude.coerceIn(-90f, 90f)
    val longitude: Float = ((longitude - 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f

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
