package com.mosaic.server.image;

public final class TileMath {
    public static int getTileX(double longitude, int zoomLevel) {

        int xtile = (int) Math.floor((longitude + 180) / 360 * (1 << zoomLevel));

        if (xtile < 0) {
            xtile = 0;
        }

        if (xtile >= (1 << zoomLevel)) {
            xtile = ((1 << zoomLevel) - 1);
        }

        return xtile;
    }

    public static int getTileY(double latitude, int zoomLevel) {

        int ytile = (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(latitude)) + 1 / Math.cos(Math.toRadians(latitude))) / Math.PI) / 2 * (1 << zoomLevel));

        if (ytile < 0) {
            ytile = 0;
        }

        if (ytile >= ( 1 << zoomLevel)) {
            ytile = ((1 << zoomLevel) - 1);
        }

        return ytile;
    }

    public static double longitudeFromTile(int tileX, int zoomLevel) {
        double n = 1 << zoomLevel;
        return tileX / n * 360.0 - 180;
    }

    public static double latitudeFromTile(int tileY, int zoomLevel) {
        double n = 1 << zoomLevel;
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1 - 2 * tileY / n))));
    }

}
