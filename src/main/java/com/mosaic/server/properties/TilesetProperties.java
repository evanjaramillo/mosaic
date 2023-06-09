package com.mosaic.server.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TilesetProperties {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String name;

    @NotBlank
    private String databasePath;

    public TilesetProperties() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }

    @Override
    public String toString() {
        return (
            "TilesetProperties{" +
            "name='" +
            name +
            '\'' +
            ", databasePath='" +
            databasePath +
            '\'' +
            '}'
        );
    }
}
