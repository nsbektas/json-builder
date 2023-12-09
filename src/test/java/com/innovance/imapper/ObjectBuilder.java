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

    public String buildJson(Request request) throws JSONException {
        if (CollectionUtils.isEmpty(fieldMappings)) {
            return "";
        }

        JSONObject output = new JSONObject();
        for (FieldMapping fieldMapping : fieldMappings) {
            Object value = null;
            if (ValueSourceType.PATH_VARIABLE.equals(fieldMapping.getValueSourceType())) {
                value = request.getPathVariables().get(fieldMapping.getValueFieldName());
            } else if (ValueSourceType.QUERY_PARAMETER.equals(fieldMapping.getValueSourceType())) {
                value = request.getQueryParameters().get(fieldMapping.getValueFieldName());
            } else if (ValueSourceType.REQUEST_BODY.equals(fieldMapping.getValueSourceType())) {
                JSONObject requestBodyJsonObject = new JSONObject(request.getBody());
                value = requestBodyJsonObject.has(fieldMapping.getValueFieldName()) ? requestBodyJsonObject.get(fieldMapping.getValueFieldName()) : null;
            }

            // Default behaviour is INCLUDE.NON_NULL
            // TODO do we need to add empty string?
            if (value != null) {
                output.put(fieldMapping.getName(), value);
            }
        }
        return output.toString();
    }
}
