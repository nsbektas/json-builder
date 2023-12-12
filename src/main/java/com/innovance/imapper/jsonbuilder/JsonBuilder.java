package com.innovance.imapper.jsonbuilder;

import com.innovance.imapper.jsonbuilder.model.Field;
import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetterFactory;
import lombok.experimental.UtilityClass;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

@UtilityClass
public class JsonBuilder {

    public static String build(Model model, ModelData modelData) {
        if (Objects.isNull(model)) {
            return new JSONObject().toString();
        }

        if (ModelType.NULL.equals(model.getType())) {
            return null;
        } else if (ModelType.EMPTY_STRING.equals(model.getType())) {
            return "";
        } else if (ModelType.EMPTY_OBJECT.equals(model.getType())) {
            return new JSONObject().toString();
        } else if (ModelType.EMPTY_LIST.equals(model.getType())) {
            return new JSONArray().toString();
        } else if (ModelType.OBJECT.equals(model.getType())) {
            return buildObjectModel(model, modelData).toString();
        } else {
            throw new IllegalArgumentException("Not Implemented Yet");
        }
    }

    private static JSONObject buildObjectModel(Model model, ModelData modelData) {
        if (Objects.isNull(model) || CollectionUtils.isEmpty(model.getFields())) {
            return new JSONObject();
        }

        JSONObject output = new JSONObject();
        for (Field field : model.getFields()) {
            Object fieldValue = null;

            if (FieldType.BASIC.equals(field.getFieldType())) {
                ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
                fieldValue = valueGetter.getValue(modelData, field.getValueSelector());
            } else if (FieldType.OBJECT.equals(field.getFieldType())) {
                fieldValue = buildObjectModel(field.getFieldModel(), modelData);
            }

            output.put(field.getName(), fieldValue);
        }

        return output;
    }
}
