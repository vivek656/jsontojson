package org.latwal.vivek.src.token;

public abstract class JsonMapperInputToken{
    protected String type;
    protected MapperToken tokenMapper;

    public String getType() {
        return type;
    }

    public MapperToken getTokenMapper() {
        return tokenMapper;
    }
}



