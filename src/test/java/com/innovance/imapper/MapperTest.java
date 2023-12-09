package com.innovance.imapper;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Map;

public class MapperTest {

    @Test
    void givenNoFieldMapping_shouldMapToEmptyString() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;
        final String expectedOutput = "";
        ObjectBuilder ob = new ObjectBuilder("serviceName");
        String output = ob.buildJson(Request.builder().body(requestBody).build());

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenOneBasicFieldMapping_shouldMapSuccessfully() throws JSONException {
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

        String output = ob.buildJson(Request.builder().body(requestBody).build());

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenTwoBasicFieldMappings_shouldMapSuccessfully() throws JSONException {
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

        String output = ob.buildJson(Request.builder().body(requestBody).build());

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicFieldMappings_whenSomeMappingValuesNotFound_shouldMapSuccessfully() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;

        final String expectedOutput = """
                {
                    "newField1" : "value1"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", "notAvailableField");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", "notAvailableField2");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping1, fieldMapping2, fieldMapping3);

        String output = ob.buildJson(Request.builder().body(requestBody).build());

        JSONAssert.assertEquals(expectedOutput, output, true);
    }


    @Test
    void givenOneQueryParamFieldMapping_shouldMapSuccessfully() throws JSONException {
        final Map<String, String> queryParameters = Map.of("queryParam1", "queryParam1Value");
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;
        Request request = new Request(queryParameters, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "queryParam1Value"
                }
                """;

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.setName("newField1");
        fieldMapping.setValueFieldName("queryParam1");
        fieldMapping.setValueSourceType(ValueSourceType.QUERY_PARAMETER);

        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    private FieldMapping createFieldMapping(String name, String valueFieldName) {
        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.setName(name);
        fieldMapping.setValueFieldName(valueFieldName);
        return fieldMapping;
    }
}
