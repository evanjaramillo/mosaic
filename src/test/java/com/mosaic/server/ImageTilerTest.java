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

import static com.mosaic.server.image.TileMath.getTileX;
import static com.mosaic.server.image.TileMath.getTileY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.mosaic.server.image.ImageTiler;
import com.mosaic.server.image.TmsBoundingBox;
import com.mosaic.server.image.TmsTree;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageTilerTest {

//    private boolean zeroSeen = false;

    @Test
    public void mock() {

        try {

            BufferedImage original = ImageIO.read(new File("/home/evan/Desktop/world.200412.3x21600x10800.png"));
            ImageTiler img = new ImageTiler();
            TmsTree<BufferedImage> tree = new TmsTree<>(); // empty tree
            img.quad(original, tree, 0); // fill the tree recursively

            BufferedImage caribbean = tree.getData(4, 5, 6);
            assertNotNull(caribbean);
            ImageIO.write(caribbean, "png", new File("target/", "caribbean.png"));

            BufferedImage asia = tree.getData(1, 1, 0);
            assertNotNull(asia);
            ImageIO.write(asia, "png", new File("target/", "asia.png"));

            // persistTree(tree); // persist the images.

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void persistTree(TmsTree<BufferedImage> tree) throws Exception {

        if (tree == null) {
            return;
        }

        TmsBoundingBox box = tree.getBoundingBox();
        int zoom = box.getLevel();
        int x = getTileX(box.getMinX(), zoom);
        int y = getTileY(box.getMaxY(), zoom);

        File f = new File("target/", String.format("%d/%d/%d.png", zoom, x, y));

        boolean ignored = f.mkdirs(); // this will fail if the dirs already exist.

        ImageIO.write(tree.getData(), "png", f);

        for (TmsTree<BufferedImage> node : tree) {
            persistTree(node);
        }

    }

}
