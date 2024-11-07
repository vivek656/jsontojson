package org.latwal.vivek.src.token;

import com.fasterxml.jackson.databind.JsonNode;
import org.latwal.vivek.src.support.JsonNodeUtils;

public class SimpleMapperToken extends MapperToken {

    public SimpleMapperToken(){
        this.tokenType = "Simple_Map";
    }
    private String inputPath;
    private String pathToMap;

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }


    public void setPathToMap(String pathToMap) {
        this.pathToMap = pathToMap;
    }

    @Override
    public JsonNode extractJsonFromInput(JsonNode node) {
        return JsonNodeUtils.getAtPath(node,inputPath);
    }

    @Override
    public String pathToMap() {
        return pathToMap;
    }

}
