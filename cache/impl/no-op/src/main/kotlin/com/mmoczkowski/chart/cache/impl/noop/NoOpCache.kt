package com.mmoczkowski.chart.cache.impl.noop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mmoczkowski.chart.cache.api.Cache

class NoOpCache<K, V> : Cache<K, V> {
    override suspend fun get(key: K, fetch: suspend (K) -> V): V = fetch(key)
}

@Composable
fun <K, V> rememberNoOpCache(): Cache<K, V> = remember { NoOpCache() }
