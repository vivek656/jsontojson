package org.latwal.vivek.src.token;

public class JsonMapperInputToken{
    protected String operationType;
    protected String pathToMap;
    protected MapperToken tokenMapper;

    public JsonMapperInputToken(
            String operationType,
            String pathToMap
    ) {
        this.operationType = operationType;
        this.pathToMap = pathToMap;
    }

    public String getPathToMap() { return pathToMap; }

    public MapperToken getTokenMapper() {
        return tokenMapper;
    }
}



