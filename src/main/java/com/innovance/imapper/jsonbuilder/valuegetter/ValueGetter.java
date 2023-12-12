package com.innovance.imapper.jsonbuilder.valuegetter;

import com.innovance.imapper.jsonbuilder.model.ModelData;

public abstract class ValueGetter {

    public abstract Object getValue(ModelData modelData, String selector);
}
