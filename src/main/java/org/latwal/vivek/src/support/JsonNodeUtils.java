package org.latwal.vivek.src.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonNodeUtils {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static ObjectMapper getMapper() {
        return  mapper;
    }

    public static JsonNode getOrThrow(JsonNode node, String key) {
        if (node.has(key)) {
            return node.get(key);
        }
        throw new IllegalArgumentException(String.format("key %s does not exist in the json object", key));
    }

    public static JsonNode getAtPath(JsonNode node, String path) {
        if (isASimplePath(path)) return node.at(path);
        if (path.contains("/--/")) {
            String pathBefore = path.substring(0, path.lastIndexOf("/--/"));
            String pathAfter = path.substring(path.lastIndexOf("/--/") + 3);
            JsonNode nodeBefore = node.at(pathBefore);
            if (!nodeBefore.isArray()) {
                throw new IllegalArgumentException(String.format("path %s is not an array", path));
            }
            ArrayNode arrayBefore = (ArrayNode) nodeBefore;
            ArrayNode resultantArray = mapper.createArrayNode();
            for (int i = 0; i < arrayBefore.size(); i++) {
                JsonNode value = arrayBefore.get(i).at(pathAfter);
                resultantArray.add(value);
            }
            return resultantArray;
        } else if (path.contains("/-/")) {
            String pathBefore = path.substring(0, path.lastIndexOf("/-/"));
            String pathAfter = path.substring(path.lastIndexOf("/-/") + 2);
            JsonNode nodeBefore = node.at(pathBefore);
            if (!nodeBefore.isArray()) {
                throw new IllegalArgumentException(String.format("path %s is not an array", path));
            }
            ArrayNode arrayBefore = (ArrayNode) nodeBefore;
            if (arrayBefore.isEmpty()) return mapper.missingNode();
            return arrayBefore.get(0).at(pathAfter);
        }
        return mapper.missingNode();
    }

    private static Boolean isASimplePath(String path) {
        return !(path.contains("/--/") || path.contains("/-/"));
    }

    public static void setValueToNode(
            ObjectNode node,
            String jsonPath,
            JsonNode value
    ) {
        String parentPath = jsonPath.substring(0, jsonPath.lastIndexOf("/"));
        String child = jsonPath.substring(jsonPath.lastIndexOf("/") + 1);
        String[] paths = parentPath.split("/");
        ObjectNode currentNode = node;
        if (paths.length != 1) {
            for (int i = 1; i < paths.length; i++) {
                currentNode.set(paths[i], mapper.createObjectNode());
                currentNode = (ObjectNode) currentNode.get(paths[i]);
            }
        }
        currentNode.set(child, value);
    }

    public static boolean containsPath(JsonNode node, String key) {
        JsonNode nodeAtPath = getAtPath(node, key);
        return nodeAtPath != null && !nodeAtPath.isMissingNode();
    }

    public static JsonNode getAtPathELseThrow(JsonNode node, String key) {
        if(!containsPath(node, key)) throw new IllegalArgumentException(String.format("key %s does not exist in the json object", key));
        return getAtPath(node, key);
    }

    public static Map<String,JsonNode> keyValueMap(JsonNode node) {
        Map<String,JsonNode> map = new HashMap<>();
        if(node.isObject()) {
            node.properties().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        } else if (node.isArray()) {
            AtomicInteger counter = new AtomicInteger();
            node.elements().forEachRemaining(json ->
                    map.put(String.valueOf(counter.getAndIncrement()) , json)
            );
        }
        return map;
    }

    public static ArrayNode combine(List<JsonNode> list) {
        return mapper.createArrayNode().addAll(list);
    }
}
