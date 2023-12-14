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
        Object output = buildModel(model, modelData);
        return output != null ? output.toString() : null;
    }

    private static Object buildModel(Model model, ModelData modelData) {
        if (Objects.isNull(model)) {
            return new JSONObject();
        }

        if (ModelType.NULL.equals(model.getType())) {
            return null;
        } else if (ModelType.EMPTY_STRING.equals(model.getType())) {
            return "";
        } else if (ModelType.EMPTY_OBJECT.equals(model.getType())) {
            return new JSONObject();
        } else if (ModelType.EMPTY_LIST.equals(model.getType())) {
            return new JSONArray();
        } else if (ModelType.OBJECT.equals(model.getType())) {
            return buildObjectModel(model, modelData);
        } else if (ModelType.BASIC.equals(model.getType())) {
            return buildBasicValue(model, modelData);
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
            Object fieldValue = buildFieldValue(field, modelData);
            output.put(field.getName(), fieldValue);
        }

        return output;
    }

    private static Object buildFieldValue(Field field, ModelData modelData) {
        Object fieldValue = null;
        if (FieldType.BASIC.equals(field.getFieldType())) {
            ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
            fieldValue = valueGetter.getValue(modelData, field.getValueSelector());
        } else if (FieldType.OBJECT.equals(field.getFieldType())) {
            fieldValue = buildObjectModel(field.getFieldModel(), modelData);
        } else if (FieldType.LIST.equals(field.getFieldType())) {
            ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
            JSONArray targetList = (JSONArray) valueGetter.getValue(modelData, field.getValueSelector());
            fieldValue = buildListModel(field.getFieldModel(), modelData, targetList);
        }

        return fieldValue;
    }

    private static JSONArray buildListModel(Model model, ModelData modelData, JSONArray targetList) {
        if (Objects.isNull(model) || CollectionUtils.isEmpty(model.getFields())) {
            return new JSONArray();
        }

        JSONArray output = new JSONArray();
        for (int i = 0; i < targetList.length(); i++) {
            Object previousTargetListItem = modelData.getTargetListItem();
            modelData.setTargetListItem(targetList.get(i));
            Object listItem = buildModel(model, modelData);
            modelData.setTargetListItem(previousTargetListItem);
            output.put(listItem);
        }

        return output;
    }

    private static Object buildBasicValue(Model model, ModelData modelData) {
        int fieldCount = CollectionUtils.isEmpty(model.getFields()) ? 0 : model.getFields().size();
        if (fieldCount != 1) {
            throw new IllegalArgumentException("Model with type ModelType.BASIC must have exactly 1 field.");
        }
        return buildFieldValue(model.getFields().get(0), modelData);
    }
}
