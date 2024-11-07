package org.latwal.vivek.src.token;

import java.util.Objects;

public class JsonMapperInputTokenSimple extends JsonMapperInputToken {

    public JsonMapperInputTokenSimple() {
        this.type = "Simple";
    }

    public void setMapperToken(MapperToken mapperToken) {
        Objects.requireNonNull(mapperToken);
        this.tokenMapper = mapperToken;
    }
}
