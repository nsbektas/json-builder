package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.repository.ParameterRepository;
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
    private ParameterRepository parameterRepository;
    private String requestBody;
    private String responseBody;

    private Object targetListItem = null;
}
