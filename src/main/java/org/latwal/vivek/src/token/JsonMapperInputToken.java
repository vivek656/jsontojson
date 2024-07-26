package org.latwal.vivek.src.token;

import java.util.Objects;

public class JsonMapperInputToken {

    private String inputPath ;
    private MapperToken mapperToken;

    public String getInputPath() {
        return inputPath;
    }

    public MapperToken getMapperToken() {
        return mapperToken;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public void setMapperToken(MapperToken mapperToken) {
        Objects.requireNonNull(mapperToken);
        this.mapperToken = mapperToken;
    }
}

