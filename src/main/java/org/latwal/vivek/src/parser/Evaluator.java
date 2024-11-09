package org.latwal.vivek.src.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.latwal.vivek.src.support.JsonNodeUtils;
import org.latwal.vivek.src.token.JsonMapperInputToken;

import java.util.*;

public class Evaluator {

    public JsonNode evaluate(JsonMapperInputToken token , JsonNode node) {
        return evaluateTokenMappingSpecs(token.getTokenMapper().getMappingSpecs() , node);
    }

    JsonNode evaluateTokenMappingSpecs(JsonNode mappingSpecs , JsonNode inputNode) {
        if(mappingIsPathMapping(mappingSpecs)){
            String inputPath = JsonNodeUtils.getAtPathELseThrow(mappingSpecs,"/path").textValue();
            return JsonNodeUtils.getAtPath(inputNode,inputPath);
        } else if (mappingIsFunction(mappingSpecs)) {
            return evaluateFunction(mappingSpecs,inputNode);
        } else if(mappingIfFunctionSpecs(mappingSpecs)){
            JsonNode functionBody = JsonNodeUtils.getOrThrow(mappingSpecs, "function_body");
            return evaluateFunction(functionBody, inputNode);
        } else {
            throw new IllegalArgumentException("unsupported mapping specs , cannot successfully extract the type of specs from the json");
        }
    }

    boolean mappingIsPathMapping(JsonNode mappingSpecs) {
       return mappingSpecs.has("path");
    }

    boolean mappingIsFunction(JsonNode mappingSpecs) {
        return mappingSpecs.has("function_name");
    }

    boolean mappingIfFunctionSpecs(JsonNode mappingSpecs) {
        return mappingSpecs.has("function_body");
    }

    JsonNode evaluateFunction(JsonNode functionMappingSpecs , JsonNode inputNode) {
        String functionName = JsonNodeUtils.getOrThrow(functionMappingSpecs, "function_name").textValue();
        switch (functionName){
            case "concat"  : {
                var args = JsonNodeUtils.getOrThrow(functionMappingSpecs, "args");
                Map<String,JsonNode> resultantArgs = new HashMap<>();
                JsonNodeUtils.keyValueMap(args).forEach((key, value) -> {
                    if (Objects.equals(key, "values")) {
                        List<JsonNode> appliedValues = JsonNodeUtils.keyValueMap(value)
                                .values().stream().map(a -> evaluateTokenMappingSpecs(a, inputNode))
                                .toList();
                        ArrayNode array = JsonNodeUtils.combine(appliedValues);
                        resultantArgs.put(key, array);
                    } else {
                        resultantArgs.put(
                                key, value
                        );
                    }
                });
                List<String> all_values = JsonNodeUtils.keyValueMap(
                        resultantArgs.getOrDefault("values" , JsonNodeUtils.combine(Collections.emptyList()))
                ).values().stream().filter(JsonNode::isTextual).map(JsonNode::textValue).toList();
                String separator = resultantArgs.getOrDefault("separator" , new TextNode("")).textValue();
                return new TextNode(String.join(separator,all_values));

            }
            case "uppercase" : {
                JsonNode args = JsonNodeUtils.getOrThrow(functionMappingSpecs, "args");
                JsonNode pathValue = evaluateTokenMappingSpecs(JsonNodeUtils.getOrThrow(args, "value") , inputNode);
                if(pathValue.isTextual()) {
                    return new TextNode(pathValue.textValue().toUpperCase(Locale.ROOT));
                } else return new TextNode("");
            }
            default: throw new IllegalArgumentException(
                    String.format("currently function %s is not supported" ,functionName )
            );
        }
    }
}