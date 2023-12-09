package com.innovance.imapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class Request {
    private final Map<String, String> queryParameters;
    private final String body;

    public Request(Map<String, String> queryParameters, String body) {
        this.queryParameters = queryParameters;
        this.body = body;
    }
}
