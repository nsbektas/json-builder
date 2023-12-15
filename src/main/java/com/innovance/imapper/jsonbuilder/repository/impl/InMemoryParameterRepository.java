package com.innovance.imapper.jsonbuilder.repository.impl;

import com.innovance.imapper.jsonbuilder.repository.ParameterRepository;

import java.util.Map;

public class InMemoryParameterRepository implements ParameterRepository {

    private final Map<String, Object> parameterMap;

    public InMemoryParameterRepository(Map<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public Object findByKey(String key) {
        return this.parameterMap.get(key);
    }
}
