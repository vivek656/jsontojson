package org.latwal.vivek.src.token;

import java.util.Objects;

public class JsonMapperInputTokenSimple extends JsonMapperInputToken {

    public JsonMapperInputTokenSimple( String pathToMap) {
        super("Simple" , pathToMap);
    }

    public void setMapperToken(MapperToken mapperToken) {
        Objects.requireNonNull(mapperToken);
        this.tokenMapper = mapperToken;
    }
}
