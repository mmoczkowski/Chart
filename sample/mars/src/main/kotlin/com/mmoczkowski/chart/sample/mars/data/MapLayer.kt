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

package com.mmoczkowski.chart.sample.mars.data

enum class MapLayer(
    val id: String,
    val title: String
) {
    Color(
        id = "Mars_Viking_MDIM21_ClrMosaic_global_232m",
        title = "Color"
    ),
    Elevation(
        id = "Mars_MGS_MOLA_ClrShade_merge_global_463m",
        title = "Elevation"
    ),
    Albedo(
        id = "msss_atlas_simp_clon",
        title = "Albedo"
    )
}
