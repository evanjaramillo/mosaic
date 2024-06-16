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

import static org.junit.Assert.fail;

import com.mosaic.server.image.ImageTiler;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageTilerTest {

    @Test
    public void mock() {
        try {

            BufferedImage original = ImageIO.read(new File("/home/evan/Desktop/world.200412.3x21600x10800.png"));

            ImageTiler img = new ImageTiler();

            TmsTree<BufferedImage> tree = new TmsTree<>();

            img.quad(original, tree, 0);

            persistTree(tree);

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
        int x = box.getTileX(box.getMinX());
        int y = box.getTileY(box.getMaxY());

        File f = new File("target/", String.format("%d/%d/%d.png", zoom, x, y));

        boolean ignored = f.mkdirs(); // this will fail if the dirs already exist.

        ImageIO.write(tree.getData(), "png", f);

        for (TmsTree<BufferedImage> node : tree) {
            persistTree(node);
        }

    }

}
