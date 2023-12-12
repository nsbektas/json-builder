package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;

public class PathVariableValueGetter extends ValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        return modelData.getPathVariables().get(selector);
    }
}
