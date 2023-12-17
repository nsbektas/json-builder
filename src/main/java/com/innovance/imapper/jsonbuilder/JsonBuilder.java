package com.innovance.imapper.jsonbuilder;

import com.innovance.imapper.jsonbuilder.model.Field;
import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;
import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetterFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class JsonBuilder {

    public static String build(Model model, ModelData modelData) {
        if (Objects.isNull(model)) {
            return new JSONObject().toString();
        }

        if (ModelType.OBJECT.equals(model.getType())) {
            return buildObjectModel(model.getFields(), modelData).toString();
        } else {
            throw new IllegalArgumentException("Not Implemented Yet");
        }
    }

    private static JSONObject buildObjectModel(List<Field> fields, ModelData modelData) {
        if (CollectionUtils.isEmpty(fields)) {
            return new JSONObject();
        }

        JSONObject output = new JSONObject();
        for (Field field : fields) {
            if (!StringUtils.isBlank(field.getName())) {
                Object fieldValue = buildFieldValue(field, modelData);
                output.put(field.getName(), fieldValue);
            }
        }

        return output;
    }

    private static JSONArray buildListModel(Field field, ModelData modelData) {
        if (Objects.isNull(field.getListItem()) || Objects.isNull(modelData)) {
            return new JSONArray();
        }

        JSONArray output = new JSONArray();
        JSONArray targetList = findTargetList(field, modelData);
        for (int i = 0; i < targetList.length(); i++) {
            Object previousTargetListItem = modelData.getTargetListItem();
            modelData.setTargetListItem(targetList.get(i));
            Object listItemFieldValue = buildFieldValue(field.getListItem(), modelData);
            modelData.setTargetListItem(previousTargetListItem);
            output.put(listItemFieldValue);
        }

        return output;
    }

    private JSONArray findTargetList(Field field, ModelData modelData) {
        ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
        Object maybeTargetList = valueGetter.getValue(modelData, field.getValueSelector());

        if (maybeTargetList instanceof JSONArray targetList) {
            return targetList;
        } else {
            return new JSONArray();
        }
    }

    private static Object buildFieldValue(Field field, ModelData modelData) {
        Object fieldValue = null;
        if (FieldType.BASIC.equals(field.getType())) {
            ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
            fieldValue = valueGetter.getValue(modelData, field.getValueSelector());
        } else if (FieldType.OBJECT.equals(field.getType())) {
            fieldValue = buildObjectModel(field.getSubfields(), modelData);
        } else if (FieldType.LIST.equals(field.getType())) {
            fieldValue = buildListModel(field, modelData);
        }

        return fieldValue;
    }
}
