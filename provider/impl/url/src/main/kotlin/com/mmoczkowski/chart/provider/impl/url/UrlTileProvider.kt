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

package com.mmoczkowski.chart.provider.impl.url

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.mmoczkowski.chart.provider.api.TileCoords
import com.mmoczkowski.chart.provider.api.TileProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import org.jetbrains.skia.Image

class UrlTileProvider internal constructor(
    private val httpClient: HttpClient,
    private val urlBuilder: (TileCoords, Int) -> String,
) : TileProvider {
    override suspend fun getTile(tile: TileCoords, tileSize: Int): ImageBitmap {
        val url: String = urlBuilder(tile, tileSize)
        val mapTileBytes: ByteArray = httpClient.get(url).body()
        return Image.makeFromEncoded(mapTileBytes).toComposeImageBitmap()
    }
}

@Composable
fun rememberUrlTileProvider(urlBuilder: (TileCoords, Int) -> String): TileProvider {
    val httpClient: HttpClient = rememberHttpClient()
    return remember(urlBuilder) { UrlTileProvider(httpClient, urlBuilder) }
}

@Composable
private fun rememberHttpClient(): HttpClient = remember {
    HttpClient(CIO)
}
