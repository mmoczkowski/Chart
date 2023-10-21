package com.mmoczkowski.chart

import androidx.compose.runtime.snapshotFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ChartStateTest {

    @Test
    fun `Given initial zoom less then min, when ChartState is constructed, then zoom should be min`() = runBlocking {
        val minZoom = 10
        val maxZoom = 20
        val chartState = ChartState(
            tileSize = 256,
            initialFocus = LatLng(0f, 0f),
            initialZoom = minZoom - 1,
            minZoom = minZoom,
            maxZoom = maxZoom,
            tilesRowCount = { 1 },
            tilesColCount = { 1 }
        )
        assertEquals(minZoom, snapshotFlow { chartState.zoom }.first())
    }

    @Test
    fun `Given initial zoom larger then max, when ChartState is constructed, then zoom should be max`() = runBlocking {
        val minZoom = 10
        val maxZoom = 20
        val chartState = ChartState(
            tileSize = 256,
            initialFocus = LatLng(0f, 0f),
            initialZoom = maxZoom + 1,
            minZoom = minZoom,
            maxZoom = maxZoom,
            tilesRowCount = { 1 },
            tilesColCount = { 1 }
        )
        assertEquals(maxZoom, snapshotFlow { chartState.zoom }.first())
    }

    @Test
    fun `Given out-of-bounds zoom, when setZoomLevel is called, then zoom should be within min and max`() = runBlocking {
        val chartState = ChartState(
            tileSize = 256,
            initialFocus = LatLng(0f, 0f),
            initialZoom = 5,
            minZoom = 0,
            maxZoom = 10,
            tilesRowCount = { 1 },
            tilesColCount = { 1 }
        )
        chartState.setZoomLevel(11)
        assertEquals(10, snapshotFlow { chartState.zoom }.first())
        chartState.setZoomLevel(-1)
        assertEquals(0, snapshotFlow { chartState.zoom }.first())
    }

    @Test
    fun `Given a zoom level, when zoomIn or zoomOut is called, then zoom should increment or decrement by 1`() = runBlocking {
        val chartState = ChartState(
            tileSize = 256,
            initialFocus = LatLng(0f, 0f),
            initialZoom = 5,
            minZoom = 0,
            maxZoom = 10,
            tilesRowCount = { 1 },
            tilesColCount = { 1 }
        )
        chartState.zoomIn()
        assertEquals(6, snapshotFlow { chartState.zoom }.first())
        chartState.zoomOut()
        assertEquals(5, snapshotFlow { chartState.zoom }.first())
    }
}
