package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Model {
    private long id;
    private String name;
    private ModelType type;
    private List<Field> fields;
}