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

package com.mosaic.server.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TilesetProperties {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String name;

    @NotBlank
    private TileSetType dataType;

    @NotBlank
    private String path;

    private TileDataCacheProperties cache;

    public TilesetProperties() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TileSetType getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = TileSetType.fromString(dataType);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TileDataCacheProperties getCache() {

        return cache;

    }

    public void setCache(TileDataCacheProperties cache) {

        this.cache = cache;

    }

    @Override
    public String toString() {
        return "TilesetProperties{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", cache=" + cache +
                '}';
    }

}
