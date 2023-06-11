package com.mosaic.server.interfaces;

public interface ITileDataProvider {

    byte[] getTileData(int zoomLevel, int column, int row);

}
