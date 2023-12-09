package com.innovance.imapper.mapper;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Request {
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

    public Object getValueFromRequestBody(String fieldName) throws JSONException {
        return bodyJsonObject.has(fieldName) ? bodyJsonObject.get(fieldName) : null;
    }
}
