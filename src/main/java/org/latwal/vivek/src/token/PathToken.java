package org.latwal.vivek.src.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.latwal.vivek.src.support.JsonNodeUtils;

public class PathToken extends MapperToken{

    public PathToken(String inputPath , JsonNode pathSpec ) {
        super(getPathMapping(inputPath));
    }

    private static JsonNode getPathMapping(String inputPath) {
        PathMappingSpecs specs = new PathMappingSpecs();
        specs.path = inputPath;
        return JsonNodeUtils.getMapper().convertValue(specs, JsonNode.class);
    }

}

class PathMappingSpecs {
    @JsonProperty("path")
    String path;
}
