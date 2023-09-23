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

package com.mosaic.server.interfaces;

import java.util.Map;

public interface IMbTilesMetadata {

    public String getName();
    public String getFormat();
    public String getJson();
    public float[] getBounds();
    public float[] getCenter();
    public int getMinZoom();
    public int getMaxZoom();
    public String getAttribution();
    public String getDescription();
    public String getType();
    public String getVersion();
    public Map<String, String> getOtherParameters();

}
