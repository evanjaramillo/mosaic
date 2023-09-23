////////////////////////////////////////////////////////////////////////////////
// mosaic - a tile map server                                                  /
// Copyright (C) 2023 Evan Jaramillo                                           /
//                                                                             /
// This program is free software: you can redistribute it and/or modify        /
// it under the terms of the GNU General Public License as published by        /
// the Free Software Foundation, either version 3 of the License, or           /
// (at your option) any later version.                                         /
//                                                                             /
// This program is distributed in the hope that it will be useful,             /
// but WITHOUT ANY WARRANTY; without even the implied warranty of              /
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               /
// GNU General Public License for more details.                                /
//                                                                             /
// You should have received a copy of the GNU General Public License           /
// along with this program.  If not, see <https://www.gnu.org/licenses/>.      /
////////////////////////////////////////////////////////////////////////////////

package com.mosaic.server;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mosaic.server.interfaces.IMbTilesDataContext;
import com.mosaic.server.mbtiles.MbTilesDataContext;
import com.mosaic.server.properties.TileDataCacheProperties;
import com.mosaic.server.properties.TileSetType;
import com.mosaic.server.properties.TilesetProperties;

public abstract class AbstractTileDataContext implements IMbTilesDataContext {

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

    public static AbstractTileDataContext createFromType(TileSetType t, TilesetProperties properties) throws Exception {

        switch (t) {
            case TILESET_MBTILES:
                return new MbTilesDataContext(properties);
            case TILESET_TMS_DIR:
            default:
                throw new UnsupportedOperationException("TMS Directory type not supported yet.");

        }

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
