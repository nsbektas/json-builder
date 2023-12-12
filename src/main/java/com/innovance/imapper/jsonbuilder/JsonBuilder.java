package com.innovance.imapper.jsonbuilder;

import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.ModelType;

public class JsonBuilder {
    private final Model model;
    private final ModelData modelData;

    public JsonBuilder(Model model, ModelData modelData) {
        this.model = model;
        this.modelData = modelData;
    }

    public String build() {
        if (ModelType.NULL.equals(model.getType())) {
            return null;
        } else if (ModelType.EMPTY_STRING.equals(model.getType())) {
            return "";
        } else if (ModelType.EMPTY_OBJECT.equals(model.getType())) {
            return "{}";
        } else if (ModelType.EMPTY_LIST.equals(model.getType())) {
            return "[]";
        } else {
            throw new IllegalArgumentException("Not Implemented Yet");
        }
    }
}
