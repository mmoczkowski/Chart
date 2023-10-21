package com.mmoczkowski.chart

import kotlin.test.Test
import kotlin.test.assertEquals

class LatLngTest {

    @Test
    fun `latitude should be coerced between -90 and 90`() {
        assertEquals(-90f, LatLng(-100f, 0f).latitude)
        assertEquals(90f, LatLng(100f, 0f).latitude)
        assertEquals(45f, LatLng(45f, 0f).latitude)
    }

    @Test
    fun `longitude should be normalized between -180 and 180`() {
        assertEquals(-180f, LatLng(0f, -180f).longitude)
        assertEquals(-179f, LatLng(0f, -179f).longitude)
        assertEquals(0f, LatLng(0f, 360f).longitude)
        assertEquals(-180f, LatLng(0f, 540f).longitude)
    }

    @Test
    fun `NullIsland should be at 0,0`() {
        assertEquals(0f, LatLng.NullIsland.latitude)
        assertEquals(0f, LatLng.NullIsland.longitude)
    }
}
