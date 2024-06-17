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

import jakarta.annotation.Nonnull;

import java.util.Iterator;
import java.util.function.Consumer;

import static com.mosaic.server.image.TileMath.latitudeFromTile;
import static com.mosaic.server.image.TileMath.longitudeFromTile;

public class TmsTree<T> implements Iterable<TmsTree<T>> {

    private final int MAX_CHILDREN = 4;
    private T data;
    private final TmsBoundingBox boundingBox;
    private TmsTree<T> northWest;
    private TmsTree<T> northEast;
    private TmsTree<T> southWest;
    private TmsTree<T> southEast;

    public TmsTree() {
        this.boundingBox = new TmsBoundingBox(); // defaults to geo bounds
    }

    public TmsTree(T data) {
        this();
        this.data = data;
    }

    public TmsTree(T data, TmsBoundingBox box) {
        this.data = data;
        this.boundingBox = box;
    }

    public boolean hasChildren() {
        return northWest != null || northEast != null || southWest != null || southEast != null;
    }

    public TmsBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public T getData() {
        return data;
    }

    // find the leaf that contains these elements
    private TmsTree<T> search(double lon, double lat, int zoom, TmsTree<T> node) {

        TmsTree<T> retTreeNode = null; // null will be returned if we make a wrong turn.

        TmsBoundingBox bb = node.getBoundingBox();
        System.out.printf("searching box %s\n", bb);// TODO REMOVE
        if (bb.contains(lon, lat)) {
            // if the current level box contains the point, we know this node or a child contains the
            // point we are looking for.
            if (!node.hasChildren() || bb.getLevel() >= zoom) {
                // no children to choose from or desired LOD is achieved.
                return node;
            }

            // search the children for the desired point
            // TODO: fix the one-off error in the y direction
            for (TmsTree<T> n : node) {
                if (n == null || !n.getBoundingBox().contains(lon, lat)) {
                    continue;
                }
                retTreeNode = search(lon, lat, zoom, n);
            }

        }

        return retTreeNode;
    }

    public T getData(int zoom, int x, int y) {

        double lon = longitudeFromTile(x, zoom);
        double lat = latitudeFromTile(y, zoom);

        TmsTree<T> node = search(lon, lat, zoom, this);

        if (node == null) {
            return null;
        }

        return node.getData();

    }

    public void setData(T data) {
        this.data = data;
    }

    public TmsTree<T> getNorthWest() {
        return northWest;
    }

    public void setNorthWest(TmsTree<T> northWest) {
        this.northWest = northWest;
    }

    public TmsTree<T> getNorthEast() {
        return northEast;
    }

    public void setNorthEast(TmsTree<T> northEast) {
        this.northEast = northEast;
    }

    public TmsTree<T> getSouthWest() {
        return southWest;
    }

    public void setSouthWest(TmsTree<T> southWest) {
        this.southWest = southWest;
    }

    public TmsTree<T> getSouthEast() {
        return southEast;
    }

    public void setSouthEast(TmsTree<T> southEast) {
        this.southEast = southEast;
    }

    public void setChild(int index, TmsTree<T> child) {
        switch (index) {
            case 0: northWest = child; break;
            case 1: northEast = child; break;
            case 2: southWest = child; break;
            case 3: southEast = child; break;
            default: throw new IndexOutOfBoundsException("Quad tree node index out of bounds: " + index);
        }
    }

    public TmsTree<T> getChild(int index) {
        return switch (index) {
            case 0 -> northWest;
            case 1 -> northEast;
            case 2 -> southWest;
            case 3 -> southEast;
            default -> throw new IndexOutOfBoundsException("Quad tree node index out of bounds: " + index);
        };
    }

    @Override
    @Nonnull
    public Iterator<TmsTree<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < MAX_CHILDREN;
            }

            @Override
            public TmsTree<T> next() {
                return getChild(index++);
            }
        };
    }

    @Override
    public void forEach(Consumer<? super TmsTree<T>> action) {
        action.accept(northWest);
        action.accept(northEast);
        action.accept(southWest);
        action.accept(southEast);
    }

}
