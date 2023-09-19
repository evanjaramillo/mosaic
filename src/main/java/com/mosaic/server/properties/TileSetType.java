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

public enum TileSetType {
    TILESET_MBTILES("TILESET_MBTILES"),
    TILESET_TMS_DIR("TILESET_TMS_DIR");

    private final String type;

    private TileSetType(final String type) {
        this.type = type;
    }

    public static TileSetType fromString(final String type) {

        switch (type.toUpperCase()) {
            case "TILESET_MBTILES":
                return TileSetType.TILESET_MBTILES;
            case "TILESET_TMS_DIR":
                return TileSetType.TILESET_TMS_DIR;
            default:
                throw new UnsupportedOperationException("Tileset type: " + type + ", is not supported yet.");
        }

    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TileSetType{" +
                "type='" + type + '\'' +
                '}';
    }

}
