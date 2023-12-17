package com.innovance.imapper.jsonbuilder.valuegetter.impl;

import com.innovance.imapper.jsonbuilder.valuegetter.ValueGetter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

@Slf4j
public abstract class JsonValueGetter extends ValueGetter {

    public static final String SUBFIELD_SEPARATOR = "->";

    public Object getValueFromJson(String json, String selector) {
        if (StringUtils.isBlank(selector)) {
            return null;
        }
        try {
            String[] orderedSelectors = selector.split(SUBFIELD_SEPARATOR);
            return getValueOrNull(new JSONObject(json), orderedSelectors);
        } catch (Exception e) {
            log.warn("Exception while getValueFromJson for json:{}, selector:{}", json, selector, e);
            return null;
        }
    }

    private Object getValueOrNull(JSONObject obj, String[] selectors) {
        JSONObject subObject = obj;
        for (int i = 0; i < selectors.length - 1; i++) {
            subObject = getJsonObjectOrNull(subObject, selectors[i]);
            if (subObject == null) {
                return null;
            }
        }

        return getJsonValueOrNull(subObject, selectors[selectors.length - 1]);
    }

    private Object getJsonValueOrNull(JSONObject obj, String key) {
        return obj.has(key) ? obj.get(key) : null;
    }

    private JSONObject getJsonObjectOrNull(JSONObject obj, String key) {
        Object maybeJsonObj = getJsonValueOrNull(obj, key);
        return maybeJsonObj instanceof JSONObject jsonObj ? jsonObj : null;
    }
}
