package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.repository.ConstantRepository;
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
    private ConstantRepository constantRepository;
    private String requestBody;
    private String responseBody;
}
