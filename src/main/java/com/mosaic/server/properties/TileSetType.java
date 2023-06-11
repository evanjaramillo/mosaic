package com.mosaic.server.properties;

public enum TileSetType {
    TILESET_MBTILES("TILESET_MBTILES"),
    TILESET_TMS_DIR("TILESET_TMS_DIR");

    private final String type;

    private TileSetType(final String type) {
        this.type = type;
    }

    public static TileSetType fromString(final String type) {

        return switch (type.toUpperCase()) {
            case "TILESET_MBTILES" -> TileSetType.TILESET_MBTILES;
            case "TILESET_TMS_DIR" -> TileSetType.TILESET_TMS_DIR;
            default -> throw new UnsupportedOperationException("Tileset type: " + type + ", is not supported yet.");
        };

    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TileSetType{" +
                "type='" + type + '\'' +
                '}';
    }

}
