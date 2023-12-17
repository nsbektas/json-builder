package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.model.ModelData;
import org.apache.commons.lang3.StringUtils;

public class TargetListItemValueGetter extends JsonValueGetter {

    @Override
    public Object getValue(ModelData modelData, String selector) {
        if (StringUtils.isEmpty(selector)) {
            return modelData.getTargetListItem();
        }

        if (modelData == null || modelData.getTargetListItem() == null) {
            return null;
        } else {
            return getValueFromJson(modelData.getTargetListItem().toString(), selector);
        }
    }
}
