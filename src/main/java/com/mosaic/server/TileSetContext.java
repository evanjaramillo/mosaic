package com.mosaic.server;

import com.mosaic.server.properties.TilesetProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;

public class TileSetContext {
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
        return template.queryForObject(query, args, types, byte[].class);
    }
}
