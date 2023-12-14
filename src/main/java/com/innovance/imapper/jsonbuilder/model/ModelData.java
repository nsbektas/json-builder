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
    private Map<String, Object> constantsMap; //TODO Could be replaced with interface like constantRepository if needed.
    private String requestBody;
    private String responseBody;
}
