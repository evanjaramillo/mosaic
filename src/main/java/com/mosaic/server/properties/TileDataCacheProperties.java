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

    public long getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(long maximumSize) {
        this.maximumSize = maximumSize;
    }

    public long getExpiryMinutes() {
        return expiryMinutes;
    }

    public void setExpiryMinutes(long expiryMinutes) {
        this.expiryMinutes = expiryMinutes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "TileDataCacheProperties{" +
                "maximumSize=" + maximumSize +
                ", expiryMinutes=" + expiryMinutes +
                ", enabled=" + enabled +
                '}';
    }

}
