package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;

public class ConstantValueGetter extends ValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        return modelData.getConstantRepository().findByKey(selector);
    }
}
