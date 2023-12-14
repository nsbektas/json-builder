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
            Object fieldValue = buildFieldValue(field, modelData);
            output.put(field.getName(), fieldValue);
        }

        return output;
    }

    private static JSONArray buildListModel(Field listItemField, ModelData modelData, JSONArray targetList) {
        if (Objects.isNull(listItemField)) {
            return new JSONArray();
        }

        JSONArray output = new JSONArray();
        for (int i = 0; i < targetList.length(); i++) {
            Object previousTargetListItem = modelData.getTargetListItem();
            modelData.setTargetListItem(targetList.get(i));
            Object listItemFieldValue = buildFieldValue(listItemField, modelData);
            modelData.setTargetListItem(previousTargetListItem);
            output.put(listItemFieldValue);
        }

        return output;
    }

    private static Object buildFieldValue(Field field, ModelData modelData) {
        Object fieldValue = null;
        if (FieldType.BASIC.equals(field.getType())) {
            ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
            fieldValue = valueGetter.getValue(modelData, field.getValueSelector());
        } else if (FieldType.OBJECT.equals(field.getType())) {
            fieldValue = buildObjectModel(field.getSubfields(), modelData);
        } else if (FieldType.LIST.equals(field.getType())) {
            ValueGetter valueGetter = ValueGetterFactory.getValueGetter(field.getValueLocation());
            JSONArray targetList = (JSONArray) valueGetter.getValue(modelData, field.getValueSelector()); //TODO add type check
            fieldValue = buildListModel(field.getListItem(), modelData, targetList);
        }

        return fieldValue;
    }
}
