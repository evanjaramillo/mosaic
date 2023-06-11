package com.mosaic.server;

import com.mosaic.server.properties.TilesetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;

public class TileSetContext {

    private final Logger logger = LoggerFactory.getLogger(TileSetContext.class);
    private final TilesetProperties properties;
    private final JdbcTemplate template;

    public TileSetContext(TilesetProperties properties, JdbcTemplate template) {
        this.properties = properties;
        this.template = template;
    }

    public byte[] getTileData(int zoom, int tileCol, int tileRow) {

        final String query = "SELECT tile_data FROM tiles WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?;";
        final Object[] args = {zoom, tileCol, tileRow};
        final int[] types = {Types.INTEGER, Types.INTEGER, Types.INTEGER};

        byte[] queryResult;

        try {
            queryResult = template.queryForObject(query, args, types, byte[].class);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No tiles found for query: x:{}, y:{}, z:{}. {}", tileCol, tileRow, zoom, e.getMessage());
            return null;
        }

        return queryResult;
    }
}
