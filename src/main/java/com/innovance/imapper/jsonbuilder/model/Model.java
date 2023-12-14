package com.innovance.imapper.jsonbuilder.model;

import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {

    private ModelType type;
    private List<Field> fields;
}