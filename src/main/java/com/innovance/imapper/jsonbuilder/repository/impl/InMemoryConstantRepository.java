package com.innovance.imapper.jsonbuilder.repository.impl;

import com.innovance.imapper.jsonbuilder.repository.ConstantRepository;

import java.util.Map;

public class InMemoryConstantRepository implements ConstantRepository {

    private final Map<String, Object> constantsMap;

    public InMemoryConstantRepository(Map<String, Object> constantsMap) {
        this.constantsMap = constantsMap;
    }

    @Override
    public Object findByKey(String key) {
        return this.constantsMap.get(key);
    }
}
