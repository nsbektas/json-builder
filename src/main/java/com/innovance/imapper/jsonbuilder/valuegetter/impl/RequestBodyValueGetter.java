package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;

public class RequestBodyValueGetter extends JsonValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        return getValueFromJson(modelData.getRequestBody(), selector);
    }
}
