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

import com.mmoczkowski.chart.LatLng

data class PointOfInterest(
    val title: String,
    val country: String,
    val date: String,
    val position: LatLng
)

internal val pointsOfInterests: List<PointOfInterest> =
    listOf(
        PointOfInterest(
            title = "Viking 1",
            country = "US",
            date = "1976-07-20",
            position = LatLng(latitude = 22.697f, longitude = -49.97f)
        ),
        PointOfInterest(
            title = "Viking 2",
            country = "US",
            date = "1976-09-03",
            position = LatLng(latitude = 48.269f, longitude = -225.99f)
        ),
        PointOfInterest(
            title = "Pathfinder",
            country = "US",
            date = "1997-07-04",
            position = LatLng(latitude = 19.33f, longitude = -33.55f)
        ),
        PointOfInterest(
            title = "Spirit",
            country = "US",
            date = "2004-01-04",
            position = LatLng(latitude = -14.568f, longitude = 175.472f)
        ),
        PointOfInterest(
            title = "Opportunity",
            country = "US",
            date = "2004-01-25",
            position = LatLng(latitude = -1.946f, longitude = -5.537f)
        ),
        PointOfInterest(
            title = "Phoenix",
            country = "US",
            date = "2008-05-25",
            position = LatLng(latitude = 68.218f, longitude = -125.749f)
        ),
        PointOfInterest(
            title = "Curiosity",
            country = "US",
            date = "2012-08-06",
            position = LatLng(latitude = -4.589f, longitude = 137.441f)
        ),
        PointOfInterest(
            title = "Perseverance",
            country = "US",
            date = "2021-02-18",
            position = LatLng(latitude = 18.444f, longitude = 77.450f)
        ),
        PointOfInterest(
            title = "Zhurong",
            country = "China",
            date = "2021-05-14",
            position = LatLng(latitude = 25.1f, longitude = 109.9f)
        )
    )