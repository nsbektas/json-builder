package com.innovance.imapper.acceptance.model;

import com.innovance.imapper.jsonbuilder.model.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ServiceMapping {
    private String serviceName;
    private String endpoint;
    private Model requestModel;
    private Model responseModel;
}
