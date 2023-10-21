# Chart: A JVM Tile Map Library for Compose Desktop

## Overview
Chart is a JVM library designed for loading and displaying map tiles using Compose Desktop. It's structured to be both
modular and adaptable, catering to a range of use-cases. Whether you're building a standard map application or something
more specialized, Chart has got you covered.

## Declaring Dependencies
To integrate Chart into your project, include the necessary dependencies:

```kotlin
val chartVersion = // Recent version
        
// Composable (required)
implementation("com.mmoczkowski:chart-ui:$chartVersion")

// Cache API (required)
implementation("com.mmoczkowski:chart-cache-api:$chartVersion")

// Cache implementations (you need at least one)
implementation("com.mmoczkowski:chart-cache-impl-lru:$chartVersion")
implementation("com.mmoczkowski:chart-cache-impl-no-op:$chartVersion")

// Tile Provider API (required)
implementation("com.mmoczkowski:chart-provider-api:$chartVersion")

// Tile Provider implementations (you need at least one)
implementation("com.mmoczkowski:chart-provider-impl-google:$chartVersion")
implementation("com.mmoczkowski:chart-provider-impl-open-street-map:$chartVersion")
implementation("com.mmoczkowski:chart-provider-impl-url:$chartVersion")
```

## Usage

Using Chart is intuitive and straightforward. Here's a simple guide to help you get started:

1. **Initialize a Cache**:
   Start by defining the type of cache you want to use. For instance, to use an LRU cache:
   ```kotlin
   val lruCache = rememberLruCache<TileCoords, ImageBitmap>(maxSize = 150)
   ```

2. **Select a Tile Provider**:
   Depending on your requirements, choose the tile provider. For using OpenStreetMap:
   ```kotlin
   val tileProvider = rememberOpenStreetMapTileProvider()
   ```

3. **Render the Map**:
   Now, simply display the map using the `Chart` composable. Here's a basic setup:
   ```kotlin
   Chart(
       provider = tileProvider,
       cache = lruCache,
   )
   ```

This code sets up a basic map using the OpenStreetMap tile provider and an LRU cache. You can customize this further by
opting for different tile providers, creating custom success/error/loading composables, or even incorporating markers.

## Project Structure
- `:build-logic:` - Build configuration and convention plugins.
- `:cache:api` - Cache API for map tile storage.
- `:cache:impl:lru` - In-memory Least Recently Used (LRU) caching implementation.
- `:cache:impl:no-op` - A mock or "no-operation" cache, useful for testing and development.
- `:provider:api` - The blueprint for the Tile Provider API.
- `:provider:impl:debug` - A debug variant of the tile provider for developmental purposes.
- `:provider:impl:google` - Google Map Tiles API tile provider implementation.
- `:provider:impl:open-street-map` - OpenStreetMap tile provider implementation.
- `:provider:impl:url` - A versatile, URL-driven tile provider adaptable to a variety of sources.
- `:ui` - The heart of the user interface, housing the Chart composables.
