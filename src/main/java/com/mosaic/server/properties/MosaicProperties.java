package com.mosaic.server.properties;

import java.util.LinkedList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mosaic")
public class MosaicProperties {
    private List<TilesetProperties> tilesets;

    public MosaicProperties() {
        this.tilesets = new LinkedList<>();
    }

    public List<TilesetProperties> getTilesets() {
        return tilesets;
    }

    public void setTilesets(List<TilesetProperties> tilesets) {
        this.tilesets = tilesets;
    }

    @Override
    public String toString() {
        // prettier-ignore
        return "MosaicProperties{" +
                "tilesets=" + tilesets +
                '}';
    }
}
