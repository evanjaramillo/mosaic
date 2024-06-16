////////////////////////////////////////////////////////////////////////////////
// mosaic - a tile map server                                                  /
// Copyright (C) 2024 Evan Jaramillo                                           /
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

import java.util.ArrayList;
import java.util.List;

public class TmsBoundingBox {

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final double centerX;
    private final double centerY;
    private int level;

    public TmsBoundingBox() {
        this.minX = -180;
        this.maxX = 180;
        this.minY = -90;
        this.maxY = 90;
        this.centerX = (minX + maxX) / 2;
        this.centerY = (minY + maxY) / 2;
    }

    public TmsBoundingBox(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.centerX = (minX + maxX) / 2;
        this.centerY = (minY + maxY) / 2;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public int getTileX(double longitude) {

        int xtile = (int) Math.floor((longitude + 180) / 360 * (1 << level));

        if (xtile < 0) {
            xtile = 0;
        }

        if (xtile >= ( 1 << level)) {
            xtile = ((1 << level) - 1);
        }

        return xtile;
    }

    public int getTileY(double latitude) {

        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(latitude)) + 1 / Math.cos(Math.toRadians(latitude))) / Math.PI) / 2 * (1 << level));

        if (ytile < 0) {
            ytile = 0;
        }

        if (ytile >= ( 1 << level)) {
            ytile = ((1 << level) - 1);
        }

        return ytile;
    }

    public List<TmsBoundingBox> subdivide() {
        List<TmsBoundingBox> quad = new ArrayList<>(4);
        quad.add(new TmsBoundingBox(minX, centerY, centerX, maxY));
        quad.add(new TmsBoundingBox(centerX, centerY, maxX, maxY));
        quad.add(new TmsBoundingBox(minX, minY, centerX, centerY));
        quad.add(new TmsBoundingBox(centerX, minY, maxX, centerY));
        return quad;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
