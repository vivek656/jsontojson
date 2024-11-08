package org.latwal.vivek.src.jsonMapper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.latwal.vivek.src.model.MappingContext;
import org.latwal.vivek.src.parser.Evaluator;
import org.latwal.vivek.src.parser.MapperParser;
import org.latwal.vivek.src.support.JsonNodeUtils;
import org.latwal.vivek.src.token.JsonMapperInputToken;

public class JsonMapper {

    private final MapperParser parser = new MapperParser();
    private final Evaluator evaluator = new Evaluator();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String mapJson(String inputJson , String mapperJson) throws Exception {
        JsonNode inputNode = objectMapper.readValue(inputJson, ObjectNode.class);
        MappingContext mappingContext = parser.parseJsonToContext(mapperJson);
        ObjectNode resultantNode = objectMapper.createObjectNode();
        for (JsonMapperInputToken token : mappingContext.getMappings()) {
            addKey(resultantNode,inputNode,token);
        }
        return objectMapper.writeValueAsString(resultantNode);
    }

    private void addKey(
            ObjectNode node,
            JsonNode inputJson,
            JsonMapperInputToken token
    ) {

        JsonNode valueInInput = evaluator.evaluate(token, inputJson);
        JsonNodeUtils.setValueToNode(node, token.getPathToMap(),  valueInInput);
    }
}
