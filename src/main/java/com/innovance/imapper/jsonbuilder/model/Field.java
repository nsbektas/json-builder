package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ValueLocation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    private long id;
    private String name;
    private FieldType fieldType;

    private ValueLocation valueLocation;
    private String valueSelector;

    private Model parentModel;
    private Model fieldModel;

}