package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class MapperToken {

    protected JsonNode mappingSpecs;

    public JsonNode getMappingSpecs() { return mappingSpecs; }
    public MapperToken(JsonNode mappingSpecs) {
        this.mappingSpecs = mappingSpecs;
    }

}


