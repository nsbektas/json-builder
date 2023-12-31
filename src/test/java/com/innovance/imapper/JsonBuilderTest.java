package com.innovance.imapper;

import com.innovance.imapper.jsonbuilder.JsonBuilder;
import com.innovance.imapper.jsonbuilder.model.Field;
import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import com.innovance.imapper.jsonbuilder.repository.ParameterRepository;
import com.innovance.imapper.jsonbuilder.repository.impl.InMemoryParameterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.innovance.imapper.jsonbuilder.model.enums.FieldType.BASIC;
import static com.innovance.imapper.jsonbuilder.model.enums.ValueLocation.*;
import static com.innovance.imapper.jsonbuilder.valuegetter.impl.JsonValueGetter.SUBFIELD_SEPARATOR;

class JsonBuilderTest {

    private ModelData modelData;

    @BeforeEach
    void setup() {
        this.modelData = createExampleModelData();
    }


    @Test
    void givenObjectModelTypeWithNoFields_shouldBuildEmptyObject() {
        Model model = Model.builder().type(ModelType.OBJECT).fields(new ArrayList<>()).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenNullModel_shouldBuildEmptyObject() {
        String output = JsonBuilder.build(null, modelData);
        String expectedOutput = """
                {
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenNullModelData_shouldBuildEmptyObject() {
        Field field1 = Field.builder().type(BASIC).name("field1").valueLocation(PATH_VARIABLE).valueSelector("field1").build();
        Field field2 = Field.builder().type(BASIC).name("field2").valueLocation(QUERY_PARAMETER).valueSelector("field2").build();
        Field field3 = Field.builder().type(BASIC).name("field3").valueLocation(PARAMETER).valueSelector("field3").build();
        Field field4 = Field.builder().type(BASIC).name("field4").valueLocation(REQUEST_BODY).valueSelector("field4").build();
        Field field5 = Field.builder().type(BASIC).name("field5").valueLocation(RESPONSE_BODY).valueSelector("field5").build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3, field4, field5)).build();

        String output = JsonBuilder.build(model, null);
        String expectedOutput = """
                {
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenModelDataWithNullFields_shouldBuildEmptyObject() {
        Field field1 = Field.builder().type(BASIC).name("field1").valueLocation(PATH_VARIABLE).valueSelector("field1").build();
        Field field2 = Field.builder().type(BASIC).name("field2").valueLocation(QUERY_PARAMETER).valueSelector("field2").build();
        Field field3 = Field.builder().type(BASIC).name("field3").valueLocation(PARAMETER).valueSelector("field3").build();
        Field field4 = Field.builder().type(BASIC).name("field4").valueLocation(REQUEST_BODY).valueSelector("field4").build();
        Field field5 = Field.builder().type(BASIC).name("field5").valueLocation(RESPONSE_BODY).valueSelector("field5").build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3, field4, field5)).build();

        String output = JsonBuilder.build(model, new ModelData());
        String expectedOutput = """
                {
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenNullSelectorValues_shouldBuildEmptyObject() {
        Field field1 = Field.builder().type(BASIC).name("field1NullSelector").valueLocation(PATH_VARIABLE).valueSelector(null).build();
        Field field2 = Field.builder().type(BASIC).name("field2NullSelector").valueLocation(QUERY_PARAMETER).valueSelector(null).build();
        Field field3 = Field.builder().type(BASIC).name("field3NullSelector").valueLocation(PARAMETER).valueSelector(null).build();
        Field field4 = Field.builder().type(BASIC).name("field4NullSelector").valueLocation(REQUEST_BODY).valueSelector(null).build();
        Field field5 = Field.builder().type(BASIC).name("field5NullSelector").valueLocation(RESPONSE_BODY).valueSelector(null).build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3, field4, field5)).build();
        ModelData modelData = createExampleModelData();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicFields_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForField1",
                    "convertedField2": "valueForField2"
                }
                """;
        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicFields_whenSomeMappingValuesNotFound_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("notAvailableField1").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("notAvailableField2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForField1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithQueryParamFields_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(QUERY_PARAMETER).valueSelector("queryParam1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(QUERY_PARAMETER).valueSelector("queryParam2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForQueryParam1",
                    "convertedField2": "valueForQueryParam2"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithQueryParamFields_whenSomeMappingValuesNotFound_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(QUERY_PARAMETER).valueSelector("queryParam1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(QUERY_PARAMETER).valueSelector("notAvailableQueryParam1").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(QUERY_PARAMETER).valueSelector("notAvailableQueryParam2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForQueryParam1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithPathVariableFields_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(PATH_VARIABLE).valueSelector("pathVariable1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(PATH_VARIABLE).valueSelector("pathVariable2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForPathVariable1",
                    "convertedField2": "valueForPathVariable2"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithPathVariableFields_whenSomeMappingValuesNotFound_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(PATH_VARIABLE).valueSelector("pathVariable1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(PATH_VARIABLE).valueSelector("notAvailablePathVariable1").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(PATH_VARIABLE).valueSelector("notAvailablePathVariable2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForPathVariable1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithParameterFields_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(PARAMETER).valueSelector("parameterKey1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(PARAMETER).valueSelector("parameterKey2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForParameterKey1",
                    "convertedField2": "valueForParameterKey2"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithParameterFields_whenSomeMappingValuesNotFound_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(PARAMETER).valueSelector("parameterKey1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(PARAMETER).valueSelector("notAvailableParameterKey1").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(PARAMETER).valueSelector("notAvailableParameterKey2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForParameterKey1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithConstantFields_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(CONSTANT).valueSelector("constantValue1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(CONSTANT).valueSelector("constantValue2").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(CONSTANT).valueSelector("").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "constantValue1",
                    "convertedField2": "constantValue2",
                    "convertedField3": ""
                }
                """;
        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicSubfieldMappings_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field2").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForObjField1.field1",
                    "convertedField2": "valueForObjField1.field2",
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicSubfieldMappings_whenSomeMappingValuesNotFound_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "notAvailableField").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("notAvailableObjField" + SUBFIELD_SEPARATOR + "field1").build();
        Field field4 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("notAvailableObjField" + SUBFIELD_SEPARATOR + "notAvailableObjField" + SUBFIELD_SEPARATOR + "notAvailableField").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3, field4)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForObjField1.field1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicSubfieldMappings_whenSomeMappingNotCorrectType_shouldBuildOnlyMatchedValues() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field1" + SUBFIELD_SEPARATOR + "field1").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("basicListField1").build();
        Field field4 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("basicListField2").build();
        Field field5 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objListField1").build();
        Field field6 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objListField1" + SUBFIELD_SEPARATOR + "field1").build();

        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3, field4, field5, field6)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": "valueForObjField1.field1"
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithObjectFieldMapping_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("field2").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field1").build();
        Field field4 = Field.builder().name("convertedField4").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1" + SUBFIELD_SEPARATOR + "field2").build();

        Field field = Field.builder().name("convertedObjectField").type(FieldType.OBJECT).subfields(List.of(field1, field2, field3, field4)).build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedObjectField": {
                        "convertedField1": "valueForField1",
                        "convertedField2": "valueForField2",
                        "convertedField3": "valueForObjField1.field1",
                        "convertedField4": "valueForObjField1.field2",
                    }
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithObjectFieldAndNoSubfields_shouldBuildEmptyObject() {
        Field field = Field.builder().name("convertedObjectField").type(FieldType.OBJECT).subfields(null).build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedObjectField": {}
                }
                """;

        System.out.println(output);
        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicFieldForObject_shouldBuildSuccessfully() {
        Field field = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objField1").build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": {
                        "field1": "valueForObjField1.field1",
                        "field2": "valueForObjField1.field2",
                        "field3": "valueForObjField1.field3"
                    }
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithBasicFieldForList_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("convertedField1").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("basicListField1").build();
        Field field2 = Field.builder().name("convertedField2").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("basicListField2").build();
        Field field3 = Field.builder().name("convertedField3").type(BASIC).valueLocation(REQUEST_BODY).valueSelector("objListField1").build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2, field3)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": [1,2,3],
                    "convertedField2": ["value1ForBasicListField2", "value2ForBasicListField2", "value3ForBasicListField2"],
                    "convertedField3": [
                        { "field1": "valueForObjListField1.row1.field1", "field2": "valueForObjListField1.row1.field2" },
                        { "field1": "valueForObjListField1.row2.field1", "field2": "valueForObjListField1.row2.field2" },
                        { "field1": "valueForObjListField1.row3.field1", "field2": "valueForObjListField1.row3.field2" },
                    ]
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithListField_whenNoListItemModel_shouldBuildSuccessfully() {
        Field basicListItemField = Field.builder().type(BASIC).valueLocation(TARGET_LIST_ITEM).valueSelector("").build();

        Field field1 = Field.builder().name("convertedField1").type(FieldType.LIST).valueLocation(REQUEST_BODY).valueSelector("basicListField1").listItem(basicListItemField).build();
        Field field2 = Field.builder().name("convertedField2").type(FieldType.LIST).valueLocation(REQUEST_BODY).valueSelector("basicListField2").listItem(basicListItemField).build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field1, field2)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField1": [1,2,3],
                    "convertedField2": ["value1ForBasicListField2", "value2ForBasicListField2", "value3ForBasicListField2"]
                }
                """;
        System.out.println(output);
        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    @Test
    void givenObjectModelTypeWithListField_whenListItemModelIsObject_shouldBuildSuccessfully() {
        Field field1 = Field.builder().name("listField1").type(BASIC).valueLocation(TARGET_LIST_ITEM).valueSelector("field1").build();
        Field field2 = Field.builder().name("listField2").type(BASIC).valueLocation(TARGET_LIST_ITEM).valueSelector("field2").build();

        Field field = Field.builder().name("convertedField").type(FieldType.LIST).valueLocation(REQUEST_BODY).valueSelector("objListField1")
                .listItem(Field.builder().type(FieldType.OBJECT).subfields(List.of(field1, field2)).build())
                .build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field)).build();

        String output = JsonBuilder.build(model, modelData);
        String expectedOutput = """
                {
                    "convertedField": [
                        { "listField1": "valueForObjListField1.row1.field1", "listField2": "valueForObjListField1.row1.field2" },
                        { "listField1": "valueForObjListField1.row2.field1", "listField2": "valueForObjListField1.row2.field2" },
                        { "listField1": "valueForObjListField1.row3.field1", "listField2": "valueForObjListField1.row3.field2" },
                    ]
                }
                """;

        JSONAssert.assertEquals(expectedOutput, output, true);
    }

    private ModelData createExampleModelData() {
        final Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("pathVariable1", "valueForPathVariable1");
        pathVariables.put("pathVariable2", "valueForPathVariable2");
        pathVariables.put("pathVariable3", "valueForPathVariable3");

        final Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("queryParam1", "valueForQueryParam1");
        queryParameters.put("queryParam2", "valueForQueryParam2");
        queryParameters.put("queryParam3", "valueForQueryParam3");

        final Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("parameterKey1", "valueForParameterKey1");
        parameterMap.put("parameterKey2", "valueForParameterKey2");
        parameterMap.put("parameterKey3", 1);
        parameterMap.put("parameterKey4", 2);

        final ParameterRepository parameterRepository = new InMemoryParameterRepository(parameterMap);

        final String requestBody = """
                {
                    "field1": "valueForField1",
                    "field2": "valueForField2",
                    "field3": "valueForField3",
                    "objField1": {
                        "field1": "valueForObjField1.field1",
                        "field2": "valueForObjField1.field2",
                        "field3": "valueForObjField1.field3"
                    },
                    "objField2": {
                        "field1": "valueForObjField2.field1",
                        "field2": "valueForObjField2.field2",
                        "field3": "valueForObjField2.field3"
                    },
                    "basicListField1": [1,2,3],
                    "basicListField2": ["value1ForBasicListField2", "value2ForBasicListField2", "value3ForBasicListField2"],
                    "objListField1": [
                        { "field1": "valueForObjListField1.row1.field1", "field2": "valueForObjListField1.row1.field2" },
                        { "field1": "valueForObjListField1.row2.field1", "field2": "valueForObjListField1.row2.field2" },
                        { "field1": "valueForObjListField1.row3.field1", "field2": "valueForObjListField1.row3.field2" },
                    ],
                    "objField3": {
                        "field1": "valueForObjField3.field1",
                        "basicListField1": [1,2,3],
                        "objListField1" : [
                            { "field1": "valueForObjField3.ObjListField1.row1.field1", "field2": "valueForObjField3.ObjListField1.row1.field2" },
                            { "field1": "valueForObjField3.ObjListField1.row2.field1", "field2": "valueForObjField3.ObjListField1.row2.field2" },
                            { "field1": "valueForObjField3.ObjListField1.row3.field1", "field2": "valueForObjField3.ObjListField1.row3.field2" }
                        ]
                    },
                    "listOfListField1": [[1,2,3],[3,4,5],[4,5,6]],
                    "listOfListField2": [
                        [
                          { "field1": "valueForListOfListField2.row1.row1.field1", "field2": "valueForListOfListField2.row1.row1.field2" },
                          { "field1": "valueForListOfListField2.row1.row2.field1", "field2": "valueForListOfListField2.row1.row2.field2" }
                        ],
                        [
                          { "field1": "valueForListOfListField2.row2.row1.field1", "field2": "valueForListOfListField2.row2.row1.field2" },
                          { "field1": "valueForListOfListField2.row2.row2.field1", "field2": "valueForListOfListField2.row2.row2.field2" }
                        ],
                    ]
                }
                """;

        final String responseBody = """
                {
                    "responseField1": "valueForResponseField1",
                    "responseField2": "valueForResponseField2",
                    "responseField3": "valueForResponseField3",
                    "responseObjField1": {
                        "responseField1": "valueForResponseObjField1.responseField1",
                        "responseField2": "valueForResponseObjField1.responseField2",
                        "responseField3": "valueForResponseObjField1.responseField3"
                    },
                    "responseObjField2": {
                        "responseField1": "valueForResponseObjField2.responseField1",
                        "responseField2": "valueForResponseObjField2.responseField2",
                        "responseField3": "valueForResponseObjField2.responseField3"
                    }
                }
                """;

        ModelData modelData = new ModelData();
        modelData.setPathVariables(pathVariables);
        modelData.setQueryParameters(queryParameters);
        modelData.setParameterRepository(parameterRepository);
        modelData.setRequestBody(requestBody);
        modelData.setResponseBody(responseBody);

        return modelData;
    }
}
