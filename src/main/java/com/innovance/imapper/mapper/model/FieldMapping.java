package com.innovance.imapper.mapper.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldMapping {

    private long id;
    private String name;
    private FieldType fieldType;
    private ValueSourceType valueSourceType;
    private String valueFieldName;
    private List<FieldMapping> subfieldMappings;

}
