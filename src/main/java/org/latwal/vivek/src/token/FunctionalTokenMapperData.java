package org.latwal.vivek.src.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.latwal.vivek.src.support.JsonNodeUtils;

import java.util.List;
import java.util.Objects;

class FunctionalTokenMapperData extends MapperToken {
    String functionName;
    String pathToMap;
    FunctionParamObject functionParams;

    FunctionalTokenMapperData(JsonNode mapperInfo) {
        JsonNode mappings = mapperInfo.get("mappings");
        this.functionName = mappings.get("function").asText();
        this.pathToMap = mapperInfo.get("pathToMap").asText();
        functionParams = getParameterObject(JsonNodeUtils.getOrThrow(mappings, "function_param"));
    }

    FunctionParamObject getParameterObject(JsonNode node) {
        if (Objects.equals(functionName, "concat")) {
            return FunctionParamObject.getParams(node,ConcatParams.class);
        } else {
            throw new UnsupportedOperationException("not supported");
        }
    }
    @Override
    public JsonNode extractJsonFromInput(JsonNode node) {
        if (Objects.equals(functionName, "concat")) {
            ConcatParams params = (ConcatParams) functionParams;
            List<String> datalist = params.paths.stream()
                    .map((a) -> JsonNodeUtils.getAtPath(node, a).textValue())
                    .filter(a -> !a.isEmpty())
                    .toList();
            if (datalist.isEmpty()) {
                return new TextNode("");
            } else {
                return new TextNode(String.join(params.separator, datalist));
            }
        } else {
            throw new UnsupportedOperationException("not supported");
        }
    }

    @Override
    public String pathToMap() {
        return pathToMap;
    }


}

class FunctionParamObject {

    static ObjectMapper mapper = new ObjectMapper();

    static <T extends FunctionParamObject> T getParams(JsonNode node , Class<T> clazz) {
        return mapper.convertValue(node, clazz);
    }
}

class ConcatParams extends FunctionParamObject {
    @JsonProperty
    public List<String> paths;
    @JsonProperty
    public String separator;
}
