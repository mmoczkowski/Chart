package com.mmoczkowski.chart.provider.api

class TileSizeUnsupportedException(val tileSize: Int) :
    Exception("Tile size of $tileSize is not supported")
