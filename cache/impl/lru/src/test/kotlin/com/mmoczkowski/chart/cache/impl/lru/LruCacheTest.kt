package com.mmoczkowski.chart.cache.impl.lru

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

class LruCacheTest {

    @Test
    fun `test cache returns fetched value if not present`() = runBlocking {
        val lruCache = LruCache<String, Int>(3)
        val key = "key1"
        val value = 42

        val result = lruCache.get(key) {
            value
        }

        assertEquals(value, result)
    }

    @Test
    fun `test cache returns cached value if present`() = runBlocking {
        val lruCache = LruCache<String, Int>(3)
        val key = "key1"
        val value = 42

        lruCache.get(key) { value }
        val cachedResult = lruCache.get(key) {
            throw Exception("Should not fetch if cached")
        }

        assertEquals(value, cachedResult)
    }

    @Test
    fun `test cache removes LRU item if max size exceeded`() = runBlocking {
        val lruCache = LruCache<String, Int>(2)

        lruCache.get("key1") { 1 }
        lruCache.get("key2") { 2 }
        lruCache.get("key3") { 3 }

        val shouldThrow = try {
            lruCache.get("key1") {
                throw Exception("key1 should have been evicted")
            }
            false
        } catch (e: Exception) {
            true
        }

        assertEquals(true, shouldThrow)
    }

    @Test
    fun `test null key should throw exception`() = runBlocking {
        val lruCache = LruCache<String?, Int>(2)

        val shouldThrow = try {
            lruCache.get(null) { 1 }
            false
        } catch (e: IllegalArgumentException) {
            true
        }

        assertEquals(true, shouldThrow)
    }

    @Test
    fun `test null fetch value should throw exception`() = runBlocking {
        val lruCache = LruCache<String, Int?>(2)
        val shouldThrow = try {
            lruCache.get("key") { null }
            false
        } catch (e: IllegalArgumentException) {
            true
        }

        assertEquals(true, shouldThrow)
    }
}
