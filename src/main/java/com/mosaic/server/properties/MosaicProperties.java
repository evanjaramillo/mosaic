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

import java.util.LinkedList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mosaic")
public class MosaicProperties {
    private List<TilesetProperties> tilesets;

    public MosaicProperties() {
        this.tilesets = new LinkedList<>();
    }

    public List<TilesetProperties> getTilesets() {
        return tilesets;
    }

    public void setTilesets(List<TilesetProperties> tilesets) {
        this.tilesets = tilesets;
    }

    @Override
    public String toString() {
        // prettier-ignore
        return "MosaicProperties{" +
                "tilesets=" + tilesets +
                '}';
    }
}
