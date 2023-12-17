package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;
import org.apache.commons.lang3.StringUtils;

public class PathVariableValueGetter extends ValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        if (modelData == null || modelData.getPathVariables() == null || StringUtils.isBlank(selector)) {
            return null;
        }

        return modelData.getPathVariables().get(selector);
    }
}