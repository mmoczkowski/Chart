package com.mmoczkowski.chart.cache.impl.lru

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mmoczkowski.chart.cache.api.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LruCache<K, V>(private val maxSize: Int) : Cache<K, V> {

    private val map: LinkedHashMap<K, V> = LinkedHashMap(maxSize, 0.75f, true)
    private val mutex: Mutex = Mutex()

    override suspend fun get(key: K, fetch: suspend (K) -> V): V {
        if(key == null) {
            throw IllegalArgumentException("Null keys are not allowed")
        }

        mutex.withLock {
            val cachedValue: V? = map[key]
            if (cachedValue != null) {
                return cachedValue
            }
        }

        val fetchedValue: V = fetch(key) ?: throw IllegalArgumentException("Null values are not allowed")
        mutex.withLock {
            val cachedValue: V? = map[key]
            return if(cachedValue == null) {
                map[key] = fetchedValue

                if(map.size > maxSize) {
                    val leastRecentlyUsed: K = map.entries.first().key
                    map.remove(leastRecentlyUsed)
                }

                fetchedValue
            } else {
                cachedValue
            }
        }
    }
}

@Composable
fun <K, V> rememberLruCache(maxSize: Int = 100): Cache<K, V> = remember(maxSize) {
    LruCache(maxSize)
}
