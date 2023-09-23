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

import com.mosaic.server.exception.MbTilesDatabaseComplianceException;
import com.mosaic.server.interfaces.IMbTilesMetadata;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MbTilesMetadata implements IMbTilesMetadata {

    private final Logger logger = LoggerFactory.getLogger(MbTilesMetadata.class);
    private final String name;
    private final String format;
    private final Map<String, String> otherParameters;
    private String json;
    private float[] bounds;
    private float[] center;
    private int minZoom;
    private int maxZoom;
    private String attribution;
    private String description;
    private String type;
    private String version;

    public MbTilesMetadata(Map<String, String> metadata) throws MbTilesDatabaseComplianceException {

        // check that the metadata table contains `name` and `format` keys.
        if (!metadata.containsKey("name")) {
            throw new MbTilesDatabaseComplianceException("Metadata did not contain a `name` row.");
        }

        this.name = metadata.get("name");
        metadata.remove("name");

        if (!metadata.containsKey("format")) {
            throw new MbTilesDatabaseComplianceException("Metadata did not contain a `format` row.");
        }

        this.format = metadata.get("format");
        metadata.remove("format");

        // if the format value is `pbf` the metadata table must contain a `json` key with valid value.
        if (this.format.equals("pbf")) {
            if (!metadata.containsKey("json")) {
                throw new MbTilesDatabaseComplianceException("Metadata `format` was pbf, but no `json` row exists.");
            }
            this.json = metadata.get("json");
            metadata.remove("json");
        }

        // string of comma-separated numbers. The maximum extent of the rendered map area in WGS84 lon,lat,lon,lat.
        if (metadata.containsKey("bounds")) {

            String bounds = metadata.get("bounds").trim();

            String[] split = bounds.split(",");

            if (split.length == 4) {

                this.bounds = new float[4];

                for (int i = 0; i < split.length; i++) {

                    try {

                        this.bounds[i] = Float.parseFloat(split[i]);

                    } catch (NumberFormatException nfe) {

                        logger.debug("{}", ExceptionUtils.getStackTrace(nfe));
                        logger.error("Error processing `bounds` element '{}' into a float.", split[i]);

                    }

                }

                metadata.remove("bounds");

            } else {

                this.bounds = null;
                logger.warn("The metadata `bounds` are malformed. Should be '%f,%f,%f,%f' was {}", bounds);

            }

        } else {
            logger.warn("The metadata is missing a recommended parameter `bounds`.");
        }

        if (metadata.containsKey("center")) {

            String center = metadata.get("center").trim();
            String[] split = center.split(",");

            if (split.length == 3) {

                this.center = new float[3];

                for (int i = 0; i < split.length; i++) {

                    try {

                        this.center[i] = Float.parseFloat(split[i]);

                    } catch (NumberFormatException nfe) {

                        logger.debug("{}", ExceptionUtils.getStackTrace(nfe));
                        logger.error("Error processing `center` element '{}' into a float.", split[i]);

                    }

                }

                metadata.remove("center");

            } else {

                this.center = null;
                logger.warn("The metadata `center` is malformed. Should be '%f,%f,%f' was {}", center);

            }

        } else {

            logger.warn("The metadata is missing a recommended parameter `center`.");

        }

        if (metadata.containsKey("minzoom")) {

            String minZoom = metadata.get("minzoom").trim();

            try {

                this.minZoom = Integer.parseInt(minZoom);
                metadata.remove("minzoom");

            } catch (NumberFormatException nfe) {

                logger.debug("{}", ExceptionUtils.getStackTrace(nfe));
                logger.error("Error processing `minzoom` element '{}' into an integer.", minZoom);

            }


        } else {

            logger.warn("The metadata is missing a recommended parameter `minzoom`.");

        }

        if (metadata.containsKey("maxzoom")) {

            String maxZoom = metadata.get("maxzoom").trim();

            try {

                this.maxZoom = Integer.parseInt(maxZoom);
                metadata.remove("maxzoom");


            } catch (NumberFormatException nfe) {

                logger.debug("{}", ExceptionUtils.getStackTrace(nfe));
                logger.error("Error processing `maxzoom` element '{}' into an integer.", maxZoom);

            }

        } else {

            logger.warn("The metadata is missing a recommended parameter `maxzoom`.");

        }

        if (metadata.containsKey("attribution")) {

            this.attribution = metadata.get("attribution");
            metadata.remove("attribution");

        } else {

            logger.info("No dataset `attribution` was found in the metadata.");

        }

        if (metadata.containsKey("description")) {

            this.description = metadata.get("description");
            metadata.remove("description");

        } else {

            logger.info("No dataset `description` was found in the metadata.");

        }

        if (metadata.containsKey("type")) {

            this.type = metadata.get("type");
            metadata.remove("type");

        } else {

            logger.info("No dataset `type` was found in the metadata.");

        }

        if (metadata.containsKey("version")) {

            this.version = metadata.get("version");
            metadata.remove("version");

        } else {

            logger.info("No dataset `version` was found in the metadata.");

        }

        this.otherParameters = metadata; // all mbtiles-specific stuff should be processed and removed.

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public String getJson() {
        return json;
    }

    @Override
    public float[] getBounds() {
        return bounds;
    }

    @Override
    public float[] getCenter() {
        return center;
    }

    @Override
    public int getMinZoom() {
        return minZoom;
    }

    @Override
    public int getMaxZoom() {
        return maxZoom;
    }

    @Override
    public String getAttribution() {
        return attribution;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Map<String, String> getOtherParameters() {
        return otherParameters;
    }

}
