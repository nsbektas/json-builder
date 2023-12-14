package com.innovance.imapper.acceptance.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ServiceMappingsDto {
    private List<ServiceMapping> serviceMappingList;
}
