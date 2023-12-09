package com.innovance.imapper;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class MapperTest {

    @Test
    void shouldMapSingleBasicField() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1"
                }
                """;

        final String expectedOutput = """
                {
                    "newField" : "value1"
                }
                """;

        FieldMapping fieldMapping = createFieldMapping("newField", "field1");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(requestBody);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void shouldMapTwoBasicFieldsFromRequestBody() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;

        final String expectedOutput = """
                {
                    "newField1" : "value1",
                    "newField2" : "value2"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", "field2");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping1, fieldMapping2);

        String output = ob.buildJson(requestBody);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    private FieldMapping createFieldMapping(String name, String valueFieldName) {
        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.setName(name);
        fieldMapping.setValueFieldName(valueFieldName);
        return fieldMapping;
    }
}
