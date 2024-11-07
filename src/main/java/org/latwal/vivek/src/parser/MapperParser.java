package org.latwal.vivek.src.parser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.latwal.vivek.src.model.MappingContext;
import org.latwal.vivek.src.support.JsonNodeUtils;
import org.latwal.vivek.src.token.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MapperParser {

    private final Logger logger = Logger.getLogger(MapperParser.class.getCanonicalName());

    private static final ObjectMapper mapper = new ObjectMapper();

    public MappingContext parseJsonToContext(String jsonAsString) throws Exception {
        JsonNode node = mapper.readValue(jsonAsString, ObjectNode.class);
        JsonNode simpleMappings = node.get("mappings");
        JsonNode complexMappings = node.get("mappings_complex");
        if(!JsonNodeUtils.containsPath(node,"/mappings") && !JsonNodeUtils.containsPath(node,"/mappings_complex"))
            throw new JsonParseException("No Mappings found");
        Set<JsonMapperInputToken> mappingsMap = new HashSet<>();
        for (Map.Entry<String, JsonNode> entry : simpleMappings.properties()) {
            mappingsMap.add(getSimpleInputMapperToken(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, JsonNode> entry : JsonNodeUtils.keyValueMap(complexMappings).entrySet()){
            mappingsMap.add(getComplexInputMapperToken(entry.getValue()));
        }
        mappingsMap = mappingsMap.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        MappingContext context = new MappingContext();
        context.setMappings(mappingsMap);
        return context;
    }

    JsonMapperInputTokenSimple getSimpleInputMapperToken(String key , JsonNode node) {
        try {
            String pathToMap = JsonNodeUtils.getOrThrow(node, "pathToMap").textValue();
            JsonMapperInputTokenSimple tokenInput = new JsonMapperInputTokenSimple();
            SimpleMapperToken token = new SimpleMapperToken();
            token.setPathToMap(pathToMap);
            token.setInputPath(key);
            tokenInput.setMapperToken(token);
            return tokenInput;
        } catch (Exception e) {
            logger.info("Error parsing json node: " + e.getMessage());
        }
        return null;
    }

    JsonMapperInputTokenComplex getComplexInputMapperToken(JsonNode node) {
        try {
           String type = JsonNodeUtils.getOrThrow(node, "type").textValue();
           JsonMapperInputTokenComplex token = new JsonMapperInputTokenComplex(type);
           token.setTokenMapperDataFromMappingInfo(node);
           return token;
        } catch (Exception e) {
            logger.info("Error parsing json node: " + e.getMessage());
        }
        return null;
    }
}
