package org.latwal.vivek.src;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.latwal.vivek.src.jsonMapper.JsonMapper;

import java.io.*;
import java.net.URL;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JsonMapperTests {

    private final JsonMapper mapper = new JsonMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void jsonMapperAbleToMapSimpleJson() throws Exception {
        String inputString = getStringFromPath("jsons/input.json");
        String mapperString = getStringFromPath("jsons/mapper.json");
        String output = mapper.mapJson(inputString, mapperString);
        JsonNode node = objectMapper.readTree(output);
    }

    private String getStringFromPath(String path) throws IOException {
        URL resourceAsStream = getClass().getClassLoader().getResource(path);
        assert resourceAsStream != null;
        return objectMapper.convertValue(objectMapper.readTree(resourceAsStream) , ObjectNode.class).toString();
    }

}
