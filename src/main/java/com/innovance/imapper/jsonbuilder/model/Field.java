package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ValueLocation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    private String name;
    private FieldType type;
    private ValueLocation valueLocation;
    private String valueSelector;

    // Subfields of Field for FieldType.OBJECT
    private List<Field> subfields;

    // List Item Field of Field for FieldType.LIST
    private Field listItem; // For List Types
}