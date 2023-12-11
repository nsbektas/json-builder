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

class MapperTest {

    @Test
    void givenNoFieldMapping_shouldMapToEmptyString() throws JSONException {
        ObjectBuilder ob = new ObjectBuilder();
        final String requestBody = """
                {
                    "field1" : "value1",
                    "field2" : "value2",
                    "field3" : "value3"
                }
                """;
        Request request = new Request(null, null, requestBody);
        final String expectedOutput = "";

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenOneBasicFieldMapping_shouldMapSuccessfully() throws JSONException {
        FieldMapping fieldMapping = createFieldMapping("newField", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenTwoBasicFieldMappings_shouldMapSuccessfully() throws JSONException {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2));

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


        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() throws JSONException {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "notAvailableField");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "notAvailableField2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2, fieldMapping3));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }


    @Test
    void givenOneQueryParamFieldMapping_shouldMapSuccessfully() throws JSONException {
        FieldMapping fieldMapping = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "queryParam1");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenQueryParamFieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() throws JSONException {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "queryParam1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam1");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.QUERY_PARAMETER, "notAvailableQueryParam2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2, fieldMapping3));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenPathVariableFieldMappings_shouldMapSuccessfully() throws JSONException {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.PATH_VARIABLE, "pathVariable1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.PATH_VARIABLE, "pathVariable2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2));

        final Map<String, String> pathVariables = Map.of("pathVariable1", "pathVariable1Value", "pathVariable2", "pathVariable2Value");
        Request request = new Request(pathVariables, null, null);

        final String expectedOutput = """
                {
                    "newField1" : "pathVariable1Value",
                    "newField2" : "pathVariable2Value"
                }
                """;

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicSubfieldMapping_shouldMapSuccessfully() {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubfieldMappings_whenSomeMappingValuesNotFound_shouldMapOnlyMatchedValues() {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field3");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj2" + SUBFIELD_SEPARATOR + "field2");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2, fieldMapping3));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubfieldMappings_whenSomeMappingsNotCorrectType_shouldMapOnylMathedValues() {
        FieldMapping fieldMapping1 = createFieldMapping("newField1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping2 = createFieldMapping("newField2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping3 = createFieldMapping("newField3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj2" + SUBFIELD_SEPARATOR + "field3");
        FieldMapping fieldMapping4 = createFieldMapping("newField4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customList" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping fieldMapping5 = createFieldMapping("newField4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObjList" + SUBFIELD_SEPARATOR + "field1");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2, fieldMapping3, fieldMapping4, fieldMapping5));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenSubObjectMapping_shouldMapSuccessfully() {
        FieldMapping fieldMapping = createFieldMapping("subObject", FieldType.OBJECT, null, null);
        FieldMapping subfieldMapping1 = createFieldMapping("field1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field1");
        FieldMapping subfieldMapping2 = createFieldMapping("field2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "field2");
        FieldMapping subfieldMapping3 = createFieldMapping("field3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field1");
        FieldMapping subfieldMapping4 = createFieldMapping("field4", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj" + SUBFIELD_SEPARATOR + "field2");

        fieldMapping.setSubfieldMappings(List.of(subfieldMapping1, subfieldMapping2, subfieldMapping3, subfieldMapping4));
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicMappingForObjectType_shouldMapSuccessfully() {
        FieldMapping fieldMapping = createFieldMapping("newCustomObj", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "customObj");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping));

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

        String output = ob.buildJson(request);

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenBasicMappingsForLists_shouldMapExactListsSuccessfully() {
        FieldMapping fieldMapping1 = createFieldMapping("list1", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "primitiveList");
        FieldMapping fieldMapping2 = createFieldMapping("list2", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "objectList");
        FieldMapping fieldMapping3 = createFieldMapping("list3", FieldType.BASIC, ValueSourceType.REQUEST_BODY, "listOfList");
        ObjectBuilder ob = new ObjectBuilder(List.of(fieldMapping1, fieldMapping2, fieldMapping3));

        final String requestBody = """
                {
                    "primitiveList" : [1,2,3],
                    "objectList" : [
                        { "field1": "li1Value1", "field2": "li1Value2" },
                        { "field1": "li2Value1", "field2": "li2Value2" }
                    ],
                    "listOfList" : [
                        [1,2,3],
                        [4,5]
                    ]
                }
                """;
        Request request = new Request(null, null, requestBody);

        final String expectedOutput = """
                {
                    "list1" : [1,2,3],
                    "list2" : [
                        { "field1": "li1Value1", "field2": "li1Value2" },
                        { "field1": "li2Value1", "field2": "li2Value2" }
                    ],
                    "list3" : [
                        [1,2,3],
                        [4,5]
                    ]
                }
                """;

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
