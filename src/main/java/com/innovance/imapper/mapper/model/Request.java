package com.innovance.imapper.mapper.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Request {
    public static final String SUBFIELD_SEPARATOR = "->";
    private final Map<String, String> pathVariables;
    private final Map<String, String> queryParameters;
    private final String body;
    private final JSONObject bodyJsonObject;

    public Request(Map<String, String> pathVariables, Map<String, String> queryParameters, String body) throws JSONException {
        this.pathVariables = pathVariables != null ? pathVariables : new HashMap<>();
        this.queryParameters = queryParameters != null ? queryParameters : new HashMap<>();

        if (body != null) {
            this.body = body;
            this.bodyJsonObject = new JSONObject(body);
        } else {
            this.body = "";
            this.bodyJsonObject = new JSONObject();
        }
    }

    public Object getValueFromPathVariables(String fieldName) {
        return pathVariables.get(fieldName);
    }

    public Object getValueFromQueryParameters(String fieldName) {
        return queryParameters.get(fieldName);
    }

    public Object getValueFromRequestBody(String fieldSelector) throws JSONException {
        String[] orderedSelectors = fieldSelector.split(SUBFIELD_SEPARATOR);

        if (orderedSelectors.length == 1) {
            return bodyJsonObject.has(orderedSelectors[0]) ? bodyJsonObject.get(orderedSelectors[0]) : null;
        }

        JSONObject subObject = bodyJsonObject;
        for (int i = 0; i < orderedSelectors.length - 1; i++) {
            if (subObject.has(orderedSelectors[i])) {
                subObject = subObject.getJSONObject(orderedSelectors[i]);
            } else {
                return null;
            }
        }

        return subObject.has(orderedSelectors[orderedSelectors.length - 1]) ?
                subObject.get(orderedSelectors[orderedSelectors.length - 1]) : null;
    }
}
