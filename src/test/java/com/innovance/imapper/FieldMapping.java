package com.innovance.imapper;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldMapping {

    private long id;
    private String name;
    private ValueSourceType valueSourceType;
    private String valueFieldName;
}
