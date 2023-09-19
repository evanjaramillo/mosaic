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

import com.mosaic.server.properties.TilesetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.Types;

public class MbTilesDataContext extends AbstractTileDataContext {

    private final Logger logger = LoggerFactory.getLogger(MbTilesDataContext.class);
    private final TilesetProperties properties;
    private JdbcTemplate jdbcTemplate;

    public MbTilesDataContext(TilesetProperties properties) {

        super(properties);
        this.properties = properties;
        initialize();

    }

    private void initialize() {

        this.jdbcTemplate = getCustomTemplate(properties.getPath());

    }

    private JdbcTemplate getCustomTemplate(String path) {

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setDatabaseName(path);
        dataSource.setUrl("jdbc:sqlite:" + path);

        return new JdbcTemplate(dataSource);
    }

    @Override
    public byte[] getTileData(int zoomLevel, int column, int row) {

        final String query = "SELECT tile_data FROM tiles WHERE zoom_level = ? AND tile_column = ? AND tile_row = ?;";
        final Object[] args = {zoomLevel, column, row};
        final int[] types = {Types.INTEGER, Types.INTEGER, Types.INTEGER};
        boolean useCache = isCacheEnabled();

        final String inputHash = getHash(column, row, zoomLevel);

        // try getting the data from cache first
        byte[] queryResult = (useCache ? getCache(inputHash) : null);

        if (queryResult != null) {
            // cache hit
            return queryResult;
        }

        try {

            queryResult = jdbcTemplate.queryForObject(query, args, types, byte[].class);

            if (useCache) {
                putCache(inputHash, queryResult);
            }

        } catch (EmptyResultDataAccessException e) {

            logger.warn("No tiles found for query: x:{}, y:{}, z:{}. {}", column, row, zoomLevel, e.getMessage());

            if (useCache) {
                putCache(inputHash, new byte[0]);
            }

            return null;

        }

        return queryResult;

    }

}
