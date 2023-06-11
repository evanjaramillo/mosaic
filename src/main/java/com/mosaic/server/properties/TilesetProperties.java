package com.mosaic.server.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TilesetProperties {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String name;

    @NotBlank
    private TileSetType dataType;

    @NotBlank
    private String path;

    private TileDataCacheProperties cache;

    public TilesetProperties() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TileSetType getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = TileSetType.fromString(dataType);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TileDataCacheProperties getCache() {

        return cache;

    }

    public void setCache(TileDataCacheProperties cache) {

        this.cache = cache;

    }

    @Override
    public String toString() {
        return "TilesetProperties{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", cache=" + cache +
                '}';
    }

}
