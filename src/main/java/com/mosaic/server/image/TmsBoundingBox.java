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

package com.mosaic.server.image;

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

    public boolean contains(double x, double y) {
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY;
    }

    public boolean exclusiveContains(double x, double y) {
        return x > this.minX && x < this.maxX && y > this.minY && y < this.maxY;
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

    @Override
    public String toString() {
        return "TmsBoundingBox{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", level=" + level +
                '}';
    }
}
