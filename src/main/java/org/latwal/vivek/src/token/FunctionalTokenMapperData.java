package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;
import org.latwal.vivek.src.support.JsonNodeUtils;

class FunctionalTokenMapperData extends MapperToken {

    FunctionalTokenMapperData(JsonNode mapperInfo) {
        super(JsonNodeUtils.getOrThrow(mapperInfo, "mapping_spec"));
        JsonNode mappings = this.mappingSpecs;
    }

}