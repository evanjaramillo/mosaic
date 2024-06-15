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

package com.mosaic.server.service;

import com.mosaic.server.AbstractTileDataContext;
import com.mosaic.server.interfaces.ILayer;
import com.mosaic.server.interfaces.IMbTilesDataContext;
import com.mosaic.server.interfaces.IMbTilesDataset;
import com.mosaic.server.interfaces.IMbTilesMetadata;
import com.mosaic.server.mbtiles.MbTilesMetadata;
import com.mosaic.server.properties.MosaicProperties;
import com.mosaic.server.properties.TilesetProperties;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MosaicService {
    private final Logger logger = LoggerFactory.getLogger(MosaicService.class);

    private final MosaicProperties properties;

    private final Map<String, AbstractTileDataContext> contexts;

    @Autowired
    public MosaicService(MosaicProperties properties) {
        this.properties = properties;
        this.contexts = new ConcurrentHashMap<>();
        initialize();
    }

    private void initialize() {
        List<TilesetProperties> tilesets = properties.getTilesets();

        if (tilesets.isEmpty()) {
            String err = "No tileset configuration provided. Nothing to do...";
            logger.error(err);
            // TODO: consider stopping the service here.
            // throw new RuntimeException(err);
        }

        for (TilesetProperties tileset : tilesets) {
            logger.info("Configuring mosaic for tileset '{}', path '{}'.", tileset.getName(), tileset.getPath());

            try {

                AbstractTileDataContext context = AbstractTileDataContext.createFromType(tileset.getDataType(), tileset);
                contexts.put(tileset.getName(), context);

            } catch (Exception e) {
                logger.debug("\n{}", ExceptionUtils.getStackTrace(e));
                logger.error("Failed to add tileset '{}'. {}", tileset.getName(), e.getMessage());
            }
        }
    }

    public IMbTilesMetadata getMetadata(String sourceData) {

        IMbTilesDataContext context = this.contexts.get(sourceData);

        if (context == null) {
            logger.error("No context available for data source: '{}'", sourceData);
            return null;
        }

        IMbTilesDataset dataset = context.getDataset();

        if (dataset == null) {
            logger.error("No dataset available for datasource '{}'", sourceData);
            return null;
        }

        return dataset.getMetadata();
    }

    public ILayer getTerrainLayerSpec(String sourceData) {

        IMbTilesDataContext context = this.contexts.get(sourceData);

        if (context == null) {
            logger.error("No context available for data source: '{}'", sourceData);
            return null;
        }

        IMbTilesDataset dataset = context.getDataset();

        if (dataset == null) {
            logger.error("No dataset available for datasource '{}'", sourceData);
            return null;
        }

        IMbTilesMetadata metadata = dataset.getMetadata();

        if (metadata == null) {
            logger.error("No metadata available for datasource '{}'", sourceData);
            return null;
        }

        return metadata.createTerrainLayer();

    }

    public Set<String> getConfiguredContexts() {
        return this.contexts.keySet();
    }

    public byte[] getTileData(String sourceData, int zoom, int col, int row) {

        if (!this.contexts.containsKey(sourceData)) {
            return null;
        }

        return this.contexts.get(sourceData).getTileData(zoom, col, row);

    }

}
