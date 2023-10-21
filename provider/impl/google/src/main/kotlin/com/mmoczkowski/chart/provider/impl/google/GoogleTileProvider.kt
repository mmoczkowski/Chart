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

package com.mmoczkowski.chart.provider.impl.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.mmoczkowski.chart.provider.api.TileCoords
import com.mmoczkowski.chart.provider.api.TileProvider
import com.mmoczkowski.chart.provider.api.TileSizeUnsupportedException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.skia.Image

class GoogleTileProvider internal constructor(
    private val apiKey: String,
    private val spec: MapSpec,
    private val httpClient: HttpClient
) : TileProvider {
    private var session: Session? = null
    private val mutex = Mutex()

    override suspend fun getTile(tile: TileCoords, tileSize: Int): ImageBitmap {
        mutex.withLock {
            if (session == null) {
                session = getSession()
            }
        }

        val bitmap: ImageBitmap = getTileBitmap(tile)
        if(bitmap.height != tileSize || bitmap.width != tileSize) {
            throw TileSizeUnsupportedException(tileSize)
        }

        return bitmap
    }

    private suspend fun getSession(): Session =
        httpClient.post("https://tile.googleapis.com/v1/createSession?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(spec)
        }.body()

    private suspend fun getTileBitmap(tile: TileCoords): ImageBitmap {
        val response: HttpResponse =
            httpClient.get(
                "https://tile.googleapis.com/v1/2dtiles/" +
                        "${tile.zoom}/" +
                        "${tile.x}/" +
                        "${tile.y}?" +
                        "session=${session?.token}&" +
                        "key=$apiKey&" +
                        "orientation=0"
            )

        mutex.withLock {
            if (response.status == HttpStatusCode.Unauthorized) {
                session = null
            }
        }

        val mapTileBytes: ByteArray = response.body()
        return Image.makeFromEncoded(mapTileBytes).toComposeImageBitmap()
    }
}

@Composable
fun rememberGoogleTileProvider(
    apiKey: String,
    mapSpec: MapSpec = MapSpec(
        mapType = "roadmap",
        language = "en-US",
        region = "US"
    )
): TileProvider {
    val httpClient: HttpClient = rememberHttpClient()
    return remember(apiKey, mapSpec) {
        GoogleTileProvider(apiKey, mapSpec, httpClient)
    }
}

@Composable
private fun rememberHttpClient(): HttpClient = remember {
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
}
