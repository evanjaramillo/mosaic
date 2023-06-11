package com.mosaic.server;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mosaic.server.interfaces.ITileDataProvider;
import com.mosaic.server.properties.TileDataCacheProperties;
import com.mosaic.server.properties.TileSetType;
import com.mosaic.server.properties.TilesetProperties;

public abstract class AbstractTileDataContext implements ITileDataProvider {

    private Cache<String, byte[]> dataCache;
    private boolean cacheEnabled;

    public AbstractTileDataContext() {

        this.dataCache = null;
        this.cacheEnabled = false;

    }

    public AbstractTileDataContext(TilesetProperties properties) {

        this();

        TileDataCacheProperties cacheProperties = properties.getCache();

        if (cacheProperties.isEnabled()) {
            this.initializeDatasetCache(cacheProperties);
        }

    }

    public static AbstractTileDataContext createFromType(TileSetType t, TilesetProperties properties) {
        return switch (t) {
            case TILESET_MBTILES -> new MbTilesDataContext(properties);
            case TILESET_TMS_DIR -> throw new UnsupportedOperationException("TMS Directory type not supported yet.");
        };
    }

    protected final void initializeDatasetCache(TileDataCacheProperties properties) {

        this.dataCache = Caffeine.newBuilder()
                .maximumSize(properties.getMaximumSize())
                .build();

        this.cacheEnabled = true;

    }

    protected final void putCache(final String hash, final byte[] blob) {

        if (this.dataCache == null) {
            return;
        }

        this.dataCache.put(hash, blob);

    }

    protected final byte[] getCache(final String hash) {

        if (this.dataCache == null) {
            return null;
        }

        return this.dataCache.getIfPresent(hash);

    }

    protected final String getHash(int x, int y, int z) {

        return x + "-" + y + "-" + z;

    }

    protected final boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    @Override
    public byte[] getTileData(int zoomLevel, int column, int row) {
        // default impl returns null.
        return null;
    }

}
