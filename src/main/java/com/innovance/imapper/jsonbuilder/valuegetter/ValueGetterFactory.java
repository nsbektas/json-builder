package com.innovance.imapper.jsonbuilder.valuegetter;

import com.innovance.imapper.jsonbuilder.model.enums.ValueLocation;
import com.innovance.imapper.jsonbuilder.valuegetter.impl.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValueGetterFactory {

    public static ValueGetter getValueGetter(ValueLocation location) {
        ValueGetter valueGetter;
        switch (location) {
            case PATH_VARIABLE -> valueGetter = new PathVariableValueGetter();
            case QUERY_PARAMETER -> valueGetter = new QueryParameterValueGetter();
            case CONSTANT -> valueGetter = new ConstantValueGetter();
            case REQUEST_BODY -> valueGetter = new RequestBodyValueGetter();
            case RESPONSE_BODY -> valueGetter = new ResponseBodyValueGetter();
            case TARGET_LIST_ITEM -> valueGetter = new TargetListItemValueGetter();
            default -> throw new IllegalArgumentException("Invalid Value Location:" + location);
        }
        return valueGetter;
    }
}
