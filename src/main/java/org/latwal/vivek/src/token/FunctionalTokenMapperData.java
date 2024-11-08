package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;
import org.latwal.vivek.src.support.JsonNodeUtils;

class FunctionalTokenMapperData extends MapperToken {
    String functionName;

    FunctionalTokenMapperData(JsonNode mapperInfo) {
        super(JsonNodeUtils.getOrThrow(mapperInfo, "mapping_spec"));
        JsonNode mappings = this.mappingSpecs;
        this.functionName = mappings.get("function_name").asText();
    }

}