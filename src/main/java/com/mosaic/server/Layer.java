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

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.mosaic.server.interfaces.ILayer;

public class Layer implements ILayer {

    private String tilejson;
    private String format;
    private String version;
    private String scheme;
    @JsonRawValue
    private String tiles;
    private String projection;
    @JsonRawValue
    private String available;
    @JsonRawValue
    private String extensions;
    private double[] bounds;

    public Layer() {
        this.tilejson = "2.1.0";
        this.format = "quantized-mesh-1.0";
        this.version = "1.0.0";
        this.scheme = "tms";
        this.tiles = "[\"{z}/{x}/{y}.terrain\"]";
        this.projection = "EPSG:4326";
        this.bounds = new double[]{-180.0, -90.0, 180.0, 90.0};
        this.available = null;
        this.extensions = null;
    }

    public String getTilejson() {
        return tilejson;
    }

    public void setTilejson(String tilejson) {
        this.tilejson = tilejson;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getTiles() {
        return tiles;
    }

    public void setTiles(String tiles) {
        this.tiles = tiles;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public double[] getBounds() {
        return bounds;
    }

    public void setBounds(double[] bounds) {
        this.bounds = bounds;
    }

}
