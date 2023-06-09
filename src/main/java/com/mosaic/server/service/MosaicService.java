package com.mosaic.server.service;

import com.mosaic.server.properties.MosaicProperties;
import com.mosaic.server.properties.TilesetProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MosaicService {
    private final Logger logger = LoggerFactory.getLogger(MosaicService.class);

    private final MosaicProperties properties;

    @Autowired
    public MosaicService(MosaicProperties properties) {
        this.properties = properties;
        initialize();
    }

    private void initialize() {
        List<TilesetProperties> tilesets = properties.getTilesets();

        if (tilesets.isEmpty()) {
            logger.warn("No tileset configuration provided. Nothing to do...");
            return;
        }

        for (TilesetProperties tileset : tilesets) {
            logger.info(
                "Configuring mosaic for tileset '{}', path '{}'.",
                tileset.getName(),
                tileset.getDatabasePath()
            );
        }
    }
}
