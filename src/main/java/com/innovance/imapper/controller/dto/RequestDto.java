package com.innovance.imapper.controller.dto;

import com.innovance.imapper.mapper.model.FieldMapping;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class RequestDto {
    private final Map<String, String> pathVariables;
    private final Map<String, String> queryParameters;
    private final String body;
    private final List<FieldMapping> fieldMappings;
}
