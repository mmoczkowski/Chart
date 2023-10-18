package com.mmoczkowski.chart.cache.api

interface Cache<K, V> {
    suspend fun get(key: K, fetch: suspend (K) -> V): V
}
