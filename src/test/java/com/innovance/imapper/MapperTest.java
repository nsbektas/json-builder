package com.innovance.imapper;

import com.innovance.imapper.mapper.ObjectBuilder;
import com.innovance.imapper.mapper.model.FieldMapping;
import com.innovance.imapper.mapper.model.Request;
import com.innovance.imapper.mapper.model.ValueSourceType;
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
        Request request = new Request(null, null, requestBody);
        final String expectedOutput = "";
        ObjectBuilder ob = new ObjectBuilder("serviceName");
        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenOneBasicFieldMapping_shouldMapSuccessfully() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1"
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField" : "value1"
                }
                """;

        FieldMapping fieldMapping = createFieldMapping("newField", ValueSourceType.REQUEST_BODY, "field1");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

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
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "value1",
                    "newField2" : "value2"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", ValueSourceType.REQUEST_BODY, "field2");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping1, fieldMapping2);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyNotNullValues() throws JSONException {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "value1"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", ValueSourceType.REQUEST_BODY, "notAvailableField");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", ValueSourceType.REQUEST_BODY, "notAvailableField2");

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        ob.addFieldMappings(fieldMapping1, fieldMapping2, fieldMapping3);

        String output = ob.buildJson(request);

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
        Request request = new Request(null, queryParameters, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "queryParam1Value"
                }
                """;

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        FieldMapping fieldMapping = createFieldMapping("newField1", ValueSourceType.QUERY_PARAMETER, "queryParam1");
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenQueryParamFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyNotNullValues() throws JSONException {
        final Map<String, String> queryParameters = Map.of("queryParam1", "queryParam1Value");
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;
        Request request = new Request(null, queryParameters, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "queryParam1Value"
                }
                """;

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        FieldMapping fieldMapping1 = createFieldMapping("newField1", ValueSourceType.QUERY_PARAMETER, "queryParam1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam1");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam2");
        ob.addFieldMappings(fieldMapping1, fieldMapping2, fieldMapping3);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenPathVariableFieldMappings_shouldMapSuccessfully() throws JSONException {
        final Map<String, String> pathVariables = Map.of("pathVariable1", "pathVariable1Value", "pathVariable2", "pathVariable2Value");
        Request request = new Request(pathVariables, null, null);

        final String expectedOutput = """
                {
                    "newField1" : "pathVariable1Value",
                    "newField2" : "pathVariable2Value"
                }
                """;

        ObjectBuilder ob = new ObjectBuilder("serviceName");
        FieldMapping fieldMapping1 = createFieldMapping("newField1", ValueSourceType.PATH_VARIABLE, "pathVariable1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", ValueSourceType.PATH_VARIABLE, "pathVariable2");
        ob.addFieldMappings(fieldMapping1, fieldMapping2);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    private FieldMapping createFieldMapping(String name, ValueSourceType valueSourceType, String valueFieldName) {
        return FieldMapping.builder()
                .name(name)
                .valueSourceType(valueSourceType)
                .valueFieldName(valueFieldName)
                .build();
    }
}
