package com.innovance.imapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

public class MapperTest {

    @Test
    void shouldMapSingleBasicField() throws JSONException {
        Map<String, Object> map = new HashMap<>();
        map.put("field1", "value1");
        String requestBody = new JSONObject(map).toString();

        Field field = new Field();
        field.setId(1l);
        field.setName("newFieldName");
        field.setValueName("field1");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFields(field);

        String output = ob.buildJson(requestBody);

        String expectedOutput = new JSONObject(Map.of("newFieldName", "value1")).toString();
        JSONAssert.assertEquals(expectedOutput, output, true);
    }
}
