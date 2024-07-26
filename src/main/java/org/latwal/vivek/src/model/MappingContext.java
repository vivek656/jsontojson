package org.latwal.vivek.src.model;

import org.latwal.vivek.src.token.JsonMapperInputToken;

import java.util.Map;

public class MappingContext {

    Map<String, JsonMapperInputToken> mappings;

    public Map<String, JsonMapperInputToken> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, JsonMapperInputToken> mappings) {
        this.mappings = mappings;
    }
}
