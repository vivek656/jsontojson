package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class MapperToken {
    protected String tokenType;

    public abstract JsonNode extractJsonFromInput(JsonNode node);

    public abstract String pathToMap();
}


