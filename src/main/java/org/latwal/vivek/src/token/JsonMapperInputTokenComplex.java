package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class JsonMapperInputTokenComplex extends JsonMapperInputToken {

    public JsonMapperInputTokenComplex(String tokenType) {
        this.type = tokenType;
    }

    public void setTokenMapperDataFromMappingInfo(JsonNode mapping) {
        if (Objects.equals(type, "function")) {
            this.tokenMapper = new FunctionalTokenMapperData(mapping);
        } else throw new IllegalArgumentException(String.format("Type %s not supported", type));
    }
}
