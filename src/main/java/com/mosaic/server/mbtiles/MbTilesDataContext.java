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

package com.mosaic.server.mbtiles;

import com.mosaic.server.AbstractTileDataContext;
import com.mosaic.server.interfaces.IMbTilesDataContext;
import com.mosaic.server.util.ImmutablePair;
import com.mosaic.server.exception.MbTilesDatabaseComplianceException;
import com.mosaic.server.interfaces.IPair;
import com.mosaic.server.properties.TilesetProperties;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.io.FileReader;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MbTilesDataContext extends AbstractTileDataContext implements IMbTilesDataContext {

    private final Logger logger = LoggerFactory.getLogger(MbTilesDataContext.class);
    private final TilesetProperties properties;
    private JdbcTemplate jdbcTemplate;
    private MbTilesDataset dataset;


    public MbTilesDataContext(TilesetProperties properties) throws MbTilesDatabaseComplianceException {

        super(properties);
        this.properties = properties;
        initialize();

    }

    private void initialize() throws MbTilesDatabaseComplianceException {

        final String path = properties.getPath();

        // check that the input database is SQLite 3 format
        if (!isSqlite3Format(path)) {
            throw new MbTilesDatabaseComplianceException("Input database was not SQLite3: " + path);
        }

        // we should have a good database to open at this point.
        this.jdbcTemplate = getCustomTemplate(path);

        this.dataset = new MbTilesDataset();

        // check that the table `metadata` exists.
        if (!tableExists("metadata")) {
            throw new MbTilesDatabaseComplianceException("Input database did not contain the required `metadata` table or view: " + path);
        }
        // TODO: verify schema `CREATE TABLE metadata (name text, value text);`
        MbTilesMetadata meta = new MbTilesMetadata(getRawMetadata());
        this.dataset.setMetadata(meta);

        // check that the table `tiles` exists
        if (!tableExists("tiles")) {
            throw new MbTilesDatabaseComplianceException("Input database did not contain the required `tiles` table or view: " + path);
        }
        // TODO: verify schema `CREATE TABLE tiles (zoom_level integer, tile_column integer, tile_row integer, tile_data blob);`

    }

    private boolean isSqlite3Format(final String path) {

        File db = new File(path);

        if (!db.exists() || !db.isFile()) {
            logger.error("Error while validating SQLite3 database. '{}' did not exist, or was not a file.", path);
            return false;
        }

        if (!db.canRead()) {
            logger.error("Error while validating SQLite3 database. No read permission to '{}'", path);
            return false;
        }

        try (FileReader reader = new FileReader(db)) {

            char[] buf = new char[16];
            int bytesRead = reader.read(buf, 0, 16);

            if (bytesRead != 16) {
                logger.error("Error while validating SQLite3 database. Could not read header.");
                return false;
            }

            String s = String.valueOf(buf);

            return s.equals("SQLite format 3\u0000");

        } catch (Exception e) {

            logger.debug("{}", ExceptionUtils.getStackTrace(e));
            logger.error("Error while validating SQLite3 database: {}", e.getMessage());
            return false;

        }

    }

    /**
     * Checks if a table or view exists in the input database.
     * @param tableName - table name to check
     * @return true if found, false if not.
     */
    private boolean tableExists(final String tableName) {

        if (this.jdbcTemplate == null) {
            logger.error("Error checking table existence. `JdbcTemplate` did not exist.");
            return false;
        }

        final String query =
                "SELECT EXISTS(SELECT 1 FROM sqlite_master WHERE (type='table' OR type='view') AND name=?);";

        Integer result = jdbcTemplate.queryForObject(query, Integer.class, tableName);

        if (result == null) {
            logger.error("Error checking table existence. Query returned null.");
            return false;
        }

        return result == 1;

    }

    private JdbcTemplate getCustomTemplate(String path) {

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        SQLiteDataSource dataSource = new SQLiteDataSource(config);
        dataSource.setDatabaseName(path);
        dataSource.setUrl("jdbc:sqlite:" + path);

        return new JdbcTemplate(dataSource);
    }

    /**
     * Gets the metadata map from the database.
     * @return Map<String, String>
     */
    private Map<String, String> getRawMetadata() {

        final String query = "SELECT * FROM metadata;";

        List<IPair<String, String>> rows = jdbcTemplate.query(query, (resultSet, rowNum) ->
                new ImmutablePair<>(resultSet.getString("name"), resultSet.getString("value")));

        Map<String, String> metadata = new HashMap<>();

        for (IPair<String, String> row : rows) {
            metadata.put(row.getKey(), row.getValue());
        }

        return metadata;

    }

    @Override
    public MbTilesDataset getDataset() {
        return this.dataset;
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

            //logger.warn("No tiles found for query: x:{}, y:{}, z:{}. {}", column, row, zoomLevel, e.getMessage());

            if (useCache) {
                putCache(inputHash, new byte[0]);
            }

            return null;

        }

        return queryResult;

    }

}
