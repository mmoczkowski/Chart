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

package com.mmoczkowski.chart.provider.impl.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import com.mmoczkowski.chart.provider.api.TileCoords
import com.mmoczkowski.chart.provider.api.TileProvider
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color4f
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Typeface

class DebugTileProvider : TileProvider {
    override suspend fun getTile(tile: TileCoords, tileSize: Int): ImageBitmap {
        val bitmap = Bitmap()
        bitmap.allocN32Pixels(tileSize, tileSize)
        val canvas = Canvas(bitmap)

        val bgPaint = Paint().setColor4f(Color4f(0.2f, 0.2f, 0.2f, 1f), null)
        canvas.drawRect(Rect(0f, 0f, tileSize.toFloat(), tileSize.toFloat()), bgPaint)

        val textPaint = Paint().setColor4f(Color4f(1f, 0f, 0f, 1f), null)
        val font = Font(Typeface.makeDefault()).apply {
            size = 20f
        }

        val textHeight = font.metrics.descent - font.metrics.ascent
        val text = "(${tile.x}, ${tile.y})"
        val textWidth = font.measureText(text, textPaint).width
        val x = (tileSize - textWidth) / 2
        val y = (tileSize + textHeight) / 2 - font.metrics.descent
        canvas.drawString(text, x, y, font, textPaint)
        return bitmap.asComposeImageBitmap()
    }
}

@Composable
fun rememberDebugTileProvider(): TileProvider = remember { DebugTileProvider() }
