package com.innovance.imapper.jsonbuilder.model;

import lombok.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ModelData {
    public Map<String, String> pathVariables;
    public Map<String, String> queryParameters;
    public String requestBody;
    public String responseBody;

    private JSONObject requestBodyJsonObject;
    private JSONObject responseBodyJsonObject;

    public static ModelData createEmptyModelData() {
        return new ModelData();
    }

    public Object getValueFromRequestBody(String fieldSelector) throws JSONException {
        String[] orderedSelectors = fieldSelector.split("->");
        if (requestBodyJsonObject == null) {
            requestBodyJsonObject = new JSONObject(requestBody);
        }
        return getValueOrNull(requestBodyJsonObject, orderedSelectors);
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
