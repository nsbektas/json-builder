package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;

public class ResponseBodyValueGetter extends JsonValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        if (modelData == null || modelData.getResponseBody() == null) {
            return null;
        }

        return getValueFromJson(modelData.getResponseBody(), selector);
    }
}
