package com.innovance.imapper;

import com.innovance.imapper.mapper.ObjectBuilder;
import com.innovance.imapper.mapper.model.FieldMapping;
import com.innovance.imapper.mapper.model.FieldType;
import com.innovance.imapper.mapper.model.Request;
import com.innovance.imapper.mapper.model.ValueSourceType;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;
import java.util.Map;

import static com.innovance.imapper.mapper.model.Request.SUBFIELD_SEPARATOR;

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
        ObjectBuilder ob = new ObjectBuilder();
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

        FieldMapping fieldMapping = createFieldMapping("newField", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");

        ObjectBuilder ob = new ObjectBuilder();
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

        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field2");

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping1, fieldMapping2);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() throws JSONException {
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

        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "notAvailableField");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "notAvailableField2");

        ObjectBuilder ob = new ObjectBuilder();
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

        ObjectBuilder ob = new ObjectBuilder();
        FieldMapping fieldMapping = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "queryParam1");
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenQueryParamFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() throws JSONException {
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

        ObjectBuilder ob = new ObjectBuilder();
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "queryParam1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam1");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam2");
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

        ObjectBuilder ob = new ObjectBuilder();
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.PATH_VARIABLE, "pathVariable1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.PATH_VARIABLE, "pathVariable2");
        ob.addFieldMappings(fieldMapping1, fieldMapping2);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicSubfieldMapping_shouldMapSuccessfully() {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "customObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    }
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "subObjectValue1",
                    "newField2" : "subObjectValue2"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field2");

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping1);
        ob.addFieldMappings(fieldMapping2);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubfieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "customObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    }
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "subObjectValue1"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field3");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj2" + SUBFIELD_SEPARATOR + "field2");

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping1);
        ob.addFieldMappings(fieldMapping2);
        ob.addFieldMappings(fieldMapping3);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubfieldMappings_whenSomeMappingsNotCorrectType_shouldMapOnylMathedValues() {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "customObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    },
                    "customList" : [
                        "str1", "str2", "str3"
                    ],
                    "customObjList" : [
                        { "field1" : "listItem1Value1", "field2" : "listItem1Value2"},
                        { "field1" : "listItem2Value1", "field2" : "listItem2Value2"},
                        { "field1" : "listItem3Value1", "field2" : "listItem3Value2"}
                    ]
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newField1" : "subObjectValue1"
                }
                """;

        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj2" + SUBFIELD_SEPARATOR + "field3");
        FieldMapping fieldMapping4 = createFieldMapping("newField4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customList" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping5 = createFieldMapping("newField4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObjList" + SUBFIELD_SEPARATOR + "field1");

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping1);
        ob.addFieldMappings(fieldMapping2);
        ob.addFieldMappings(fieldMapping3);
        ob.addFieldMappings(fieldMapping4);
        ob.addFieldMappings(fieldMapping5);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubObjectMapping_shouldMapSuccessfully() {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "customObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    }
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "subObject" : {
                        "field1" : "value1",
                        "field2" : "value2",
                        "field3" : "subObjectValue1",
                        "field4" : "subObjectValue2"
                    }
                }
                """;

        FieldMapping fieldMapping = createFieldMapping("subObject", FieldType.OBJECT, null, null);

        FieldMapping subfieldMapping1 = createFieldMapping("field1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping subfieldMapping2 = createFieldMapping("field2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field2");
        FieldMapping subfieldMapping3 = createFieldMapping("field3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping subfieldMapping4 = createFieldMapping("field4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field2");

        fieldMapping.setSubfieldMappings(List.of(subfieldMapping1, subfieldMapping2, subfieldMapping3, subfieldMapping4));

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }
    
    @Test
    void givenBasicMappingForObjectType_shouldMapSuccessfully() {
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "customObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    }
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "newCustomObj" : {
                        "field1": "subObjectValue1",
                        "field2": "subObjectValue2"
                    }
                }
                """;

        FieldMapping fieldMapping = createFieldMapping("newCustomObj", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj");

        ObjectBuilder ob = new ObjectBuilder();
        ob.addFieldMappings(fieldMapping);

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }


    private FieldMapping createFieldMapping(String name, FieldType fieldType, ValueSourceType valueSourceType, String valueFieldName) {
        return FieldMapping.builder()
                .name(name)
                .fieldType(fieldType)
                .valueSourceType(valueSourceType)
                .valueFieldName(valueFieldName)
                .build();
    }
}
