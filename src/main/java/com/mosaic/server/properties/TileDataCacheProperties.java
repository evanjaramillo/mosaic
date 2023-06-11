package com.mosaic.server.properties;

import jakarta.validation.constraints.Min;

public class TileDataCacheProperties {

    @Min(1)
    private long maximumSize;

    @Min(1)
    private long expiryMinutes;

    private boolean enabled;

    public TileDataCacheProperties() {

    }
}
