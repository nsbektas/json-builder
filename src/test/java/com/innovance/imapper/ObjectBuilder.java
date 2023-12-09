package com.innovance.imapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    private String serviceName;
    private List<FieldMapping> fieldMappings;

    public ObjectBuilder(String serviceName) {
        this.serviceName = serviceName;
    }

    public void addFieldMappings(FieldMapping... fieldMappings) {
        if (this.fieldMappings == null) {
            this.fieldMappings = new ArrayList<>();
        }
        this.fieldMappings.addAll(List.of(fieldMappings));
    }

    public String buildJson(String requestBody) throws JSONException {
        if (CollectionUtils.isEmpty(fieldMappings)) {
            return "";
        }
        
        JSONObject requestBodyJsonObject = new JSONObject(requestBody);
        JSONObject output = new JSONObject();
        for (FieldMapping fieldMapping : fieldMappings) {
            Object value = requestBodyJsonObject.get(fieldMapping.getValueFieldName());
            output.put(fieldMapping.getName(), value);
        }
        return output.toString();
    }
}
