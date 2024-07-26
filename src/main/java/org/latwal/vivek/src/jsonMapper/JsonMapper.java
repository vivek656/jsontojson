package org.latwal.vivek.src.jsonMapper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.latwal.vivek.src.model.MappingContext;
import org.latwal.vivek.src.parser.MapperParser;
import org.latwal.vivek.src.support.JsonNodeUtils;
import org.latwal.vivek.src.token.JsonMapperInputToken;

import java.util.Map;

public class JsonMapper {

    private final MapperParser parser = new MapperParser();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String mapJson(String inputJson , String mapperJson) throws Exception {
        JsonNode inputNode = objectMapper.readValue(inputJson, ObjectNode.class);
        MappingContext mappingContext = parser.parseJsonToContext(mapperJson);
        ObjectNode resultantNode = objectMapper.createObjectNode();

        for (Map.Entry<String, JsonMapperInputToken> entry : mappingContext.getMappings().entrySet()) {
            String key = entry.getKey();
            JsonMapperInputToken value = entry.getValue();
            addKey(resultantNode, inputNode, key, value);
        }

        return objectMapper.writeValueAsString(resultantNode);
    }

    private void addKey(
            ObjectNode node,
            JsonNode inputJson,
            String key,
            JsonMapperInputToken token
    ) throws IllegalAccessException {
        JsonNode valueInInput = JsonNodeUtils.getAtPath(inputJson, key);
        JsonNodeUtils.setValueToNode(node, token.getMapperToken().getPathToMap(), valueInInput);
    }
}
