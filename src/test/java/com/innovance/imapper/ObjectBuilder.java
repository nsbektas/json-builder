package com.innovance.imapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    private String serviceName;
    private List<Field> fields;

    public ObjectBuilder(String serviceName) {
        this.serviceName = serviceName;
    }

    public void addFields(Field field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    public String buildJson(String requestBody) throws JSONException {
        JSONObject requestBodyJsonObject = new JSONObject(requestBody);
        JSONObject output = new JSONObject();
        for (Field field : fields) {
            Object value = requestBodyJsonObject.get(field.getValueName());
            output.put(field.getName(), value);
        }
        return output.toString();
    }
}
