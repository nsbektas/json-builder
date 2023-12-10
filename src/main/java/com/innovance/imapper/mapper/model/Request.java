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
        return getValueOrNull(bodyJsonObject, orderedSelectors);
    }

    //TODO Maybe move this helper methods into something like JsonHelper etc.
    private Object getValueOrNull(JSONObject obj, String[] selectors) {
        JSONObject subObject = obj;
        for (int i = 0; i < selectors.length - 1; i++) {
            subObject = getJsonObjectOrNull(subObject, selectors[i]);
            if (subObject == null) {
                return null;
            }
        }

        return getJsonValueOrNull(subObject, selectors[selectors.length - 1]);
    }

    private Object getJsonValueOrNull(JSONObject obj, String key) {
        return obj.has(key) ? obj.get(key) : null;
    }

    private JSONObject getJsonObjectOrNull(JSONObject obj, String key) {
        Object maybeJsonObj = getJsonValueOrNull(obj, key);
        return maybeJsonObj instanceof JSONObject jsonObj ? jsonObj : null;
    }
}
