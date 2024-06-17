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


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageTiler {

    private final int tileSizeX;
    private final int tileSizeY;

    public ImageTiler() {
        this.tileSizeX = 256;
        this.tileSizeY = 256;
    }

    public ImageTiler(int tileSizeX, int tileSizeY) {
        this.tileSizeX = tileSizeX;
        this.tileSizeY = tileSizeY;
    }

    public BufferedImage resize(int x, int y, BufferedImage original) {
        BufferedImage resized = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resized.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(original, 0, 0, x, y, null);
        graphics2D.dispose();
        return resized;
    }

    public void quad(BufferedImage original, TmsTree<BufferedImage> tree, int level) {

        if (original == null || level < 0) {
            throw new IllegalStateException("image file was null");
        }

        TmsBoundingBox box = tree.getBoundingBox();
        box.setLevel(level);

        // we want to store the resized image for this level at this node.
        tree.setData(resize(this.tileSizeX, this.tileSizeY, original));

        // begin the subdivision logic.
        int imageHeight = original.getHeight(),
            imageWidth = original.getWidth();

        // the recursion will stop when the size of the remaining images is less than the requested
        // tile sizes.
        if (imageWidth > this.tileSizeX && imageHeight > this.tileSizeY) {

            List<TmsBoundingBox> subdivision = box.subdivide();

            int imageHalfHeight = imageHeight / 2,
                imageHalfWidth = imageWidth / 2;

            for (int i = 0; i < 4; i++) {

                int xOffset = (i % 2) * imageHalfWidth;
                int yOffset = (i / 2) * imageHalfHeight;

                TmsBoundingBox bb = subdivision.get(i);

                // full-scale sub image
                BufferedImage subImage = original.getSubimage(xOffset, yOffset, imageHalfWidth, imageHalfHeight);
                // size-compliant sub image
                BufferedImage resized = resize(this.tileSizeX, this.tileSizeY, subImage); // force to fixed size

                TmsTree<BufferedImage> node = new TmsTree<>(resized, bb);
                tree.setChild(i, node);

                quad(subImage, node, level + 1);

            }

        }

    }

}
