package com.innovance.imapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class Request {
    private final Map<String, String> pathVariables;
    private final Map<String, String> queryParameters;
    private final String body;

    public Request(Map<String, String> pathVariables, Map<String, String> queryParameters, String body) {
        this.pathVariables = pathVariables != null ? pathVariables : new HashMap<>();
        this.queryParameters = queryParameters != null ? queryParameters : new HashMap<>();
        this.body = body != null ? body : "";
    }
}
