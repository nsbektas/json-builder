package com.innovance.imapper.mapper;

import com.innovance.imapper.mapper.model.FieldMapping;
import com.innovance.imapper.mapper.model.FieldType;
import com.innovance.imapper.mapper.model.Request;
import com.innovance.imapper.mapper.model.ValueSourceType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder {

    private List<FieldMapping> fieldMappings;

    public ObjectBuilder() {
        this.fieldMappings = new ArrayList<>();
    }

    public ObjectBuilder(List<FieldMapping> fieldMappings) {
        this.fieldMappings = fieldMappings != null ? fieldMappings : new ArrayList<>();
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
            if (FieldType.BASIC.equals(fieldMapping.getFieldType())) {
                if (ValueSourceType.PATH_VARIABLE.equals(fieldMapping.getValueSourceType())) {
                    value = request.getValueFromPathVariables(fieldMapping.getValueFieldName());
                } else if (ValueSourceType.QUERY_PARAMETER.equals(fieldMapping.getValueSourceType())) {
                    value = request.getValueFromQueryParameters(fieldMapping.getValueFieldName());
                } else if (ValueSourceType.REQUEST_BODY.equals(fieldMapping.getValueSourceType())) {
                    value = request.getValueFromRequestBody(fieldMapping.getValueFieldName());
                }
            } else if (FieldType.OBJECT.equals(fieldMapping.getFieldType())) {
                ObjectBuilder subObjectBuilder = new ObjectBuilder(fieldMapping.getSubfieldMappings());
                value = new JSONObject(subObjectBuilder.buildJson(request));
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
