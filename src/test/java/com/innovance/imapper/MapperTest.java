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

    @Test
    void shouldMapTwoBasicFieldsFromRequestBody() throws JSONException {
        Map<String, Object> map = new HashMap<>();
        map.put("field1", "value1");
        map.put("field2", "value2");
        map.put("field3", "value3");
        String requestBody = new JSONObject(map).toString();

        Field field1 = new Field();
        field1.setId(1l);
        field1.setName("newField1");
        field1.setValueName("field1");

        Field field2 = new Field();
        field2.setId(2l);
        field2.setName("newField2");
        field2.setValueName("field2");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFields(field1, field2);

        String output = ob.buildJson(requestBody);

        String expectedOutput = new JSONObject(Map.of("newField1", "value1", "newField2", "value2")).toString();
        JSONAssert.assertEquals(expectedOutput, output, true);
    }
}
