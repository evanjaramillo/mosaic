package com.mosaic.server.service;

import com.mosaic.server.TileSetContext;
import com.mosaic.server.properties.MosaicProperties;
import com.mosaic.server.properties.TilesetProperties;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

@Service
public class MosaicService {
    private final Logger logger = LoggerFactory.getLogger(MosaicService.class);

    private final MosaicProperties properties;

    private final Map<String, TileSetContext> contexts;

    @Autowired
    public MosaicService(MosaicProperties properties) {
        this.properties = properties;
        this.contexts = new ConcurrentHashMap<>();
        initialize();
    }

    private void initialize() {
        List<TilesetProperties> tilesets = properties.getTilesets();

        if (tilesets.isEmpty()) {
            logger.warn("No tileset configuration provided. Nothing to do...");
            return;
        }

        for (TilesetProperties tileset : tilesets) {
            logger.info("Configuring mosaic for tileset '{}', path '{}'.", tileset.getName(), tileset.getDatabasePath());

            try {

                contexts.put(tileset.getName(), new TileSetContext(tileset,
                        getCustomTemplate(tileset.getDatabasePath())));

            } catch (Exception e) {
                logger.debug("\n{}", ExceptionUtils.getStackTrace(e));
                logger.error("Failed to add tileset '{}'. {}", tileset.getName(), e.getMessage());
            }
        }
    }

    private JdbcTemplate getCustomTemplate(String path) {

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setDatabaseName(path);
        dataSource.setUrl("jdbc:sqlite:" + path);

        return new JdbcTemplate(dataSource);
    }

    public byte[] getTileData(String sourceData, int zoom, int col, int row) {

        if (!this.contexts.containsKey(sourceData)) {
            logger.warn("Tileset '{}' was requested, but not configured.", sourceData);
            return null;
        }

        logger.debug("Requested tile from '{}' at x:{}, y:{}, z:{}", sourceData, col, row, zoom);

        return this.contexts.get(sourceData).getTileData(zoom, col, row);

    }

}
