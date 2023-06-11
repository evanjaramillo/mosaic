package com.mosaic.server;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mosaic.server.interfaces.ITileDataProvider;
import com.mosaic.server.properties.TileDataCacheProperties;

public abstract class AbstractTileDataContext implements ITileDataProvider {

    private Cache<String, byte[]> dataCache;

    public AbstractTileDataContext() {

        this.dataCache = null;

    }

    protected void initializeDatasetCache(TileDataCacheProperties properties) {

    }

    @Override
    public byte[] getTileData(int zoomLevel, int column, int row) {
        // default impl returns null.
        return null;
    }

}
