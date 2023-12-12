package com.innovance.imapper.jsonbuilder.model;

import lombok.*;

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

    public static ModelData createEmptyModelData() {
        return new ModelData();
    }

}
