package org.latwal.vivek.src.parser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.latwal.vivek.src.model.MappingContext;
import org.latwal.vivek.src.support.JsonNodeUtils;
import org.latwal.vivek.src.token.JsonMapperInputToken;
import org.latwal.vivek.src.token.MapperToken;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MapperParser {

    private final Logger logger = Logger.getLogger(MapperParser.class.getCanonicalName());

    private static final ObjectMapper mapper = new ObjectMapper();

    public MappingContext parseJsonToContext(String jsonAsString) throws Exception {
        JsonNode node = mapper.readValue(jsonAsString, ObjectNode.class);
        JsonNode mappings = node.get("mappings");
        if(mappings == null || mappings.isMissingNode())
            throw new JsonParseException("No Mappings found");
        Map<String, JsonMapperInputToken> mappingsMap = new HashMap<>();
        for (Map.Entry<String, JsonNode> entry : mappings.properties()) {
            MapperToken mappertoken = mapperTokenFromJsonOrNull(entry.getValue());
            if(mappertoken == null) continue;
            JsonMapperInputToken token = new JsonMapperInputToken();
            token.setInputPath(entry.getKey());
            token.setMapperToken(mappertoken);
            mappingsMap.put(entry.getKey(), token);
        }
        MappingContext context = new MappingContext();
        context.setMappings(mappingsMap);
        return context;
    }

    MapperToken mapperTokenFromJsonOrNull(JsonNode node) {
        try {
            String pathToMap = JsonNodeUtils.getOrThrow(node, "pathToMap").textValue();
            MapperToken token = new MapperToken();
            token.setPathToMap(pathToMap);
            return token;
        } catch (Exception e) {
            logger.info("Error parsing json node: " + e.getMessage());
        }
        return null;
    }
}
