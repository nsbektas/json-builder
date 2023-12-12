package com.innovance.imapper.jsonbuilder.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ModelData {
    private Map<String, String> pathVariables;
    private Map<String, String> queryParameters;
    private String requestBody;
    private String responseBody;
}
