package org.latwal.vivek.src.model;

import org.latwal.vivek.src.token.JsonMapperInputToken;

import java.util.Set;

public class MappingContext {

    Set<JsonMapperInputToken> mappings;

    public Set<JsonMapperInputToken> getMappings() {
        return mappings;
    }

    public void setMappings(Set<JsonMapperInputToken> mappings) {
        this.mappings = mappings;
    }
}
