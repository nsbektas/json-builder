package com.innovance.imapper.acceptance;

import com.innovance.imapper.jsonbuilder.JsonBuilder;
import com.innovance.imapper.jsonbuilder.model.Field;
import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.enums.FieldType;
import com.innovance.imapper.jsonbuilder.model.enums.ModelType;
import com.innovance.imapper.jsonbuilder.model.enums.ValueLocation;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AcceptanceTest {

    @Test
    void test() {
        ModelData modelData = createExampleModelData();

        Field field = Field.builder().name("SEARCHTITLE").type(FieldType.BASIC).valueLocation(ValueLocation.PATH_VARIABLE).valueSelector("IdentificationNumber").build();
        Model model = Model.builder().type(ModelType.OBJECT).fields(List.of(field)).build();

        final String backendRequest = JsonBuilder.build(model, modelData);
        final String expectedBackendRequest = """
                {
                    "SEARCHTITLE": "21498572558"
                }
                """;

        JSONAssert.assertEquals(expectedBackendRequest, expectedBackendRequest, true);


        Field valueModelField1 = Field.builder().name("Name").type(FieldType.BASIC).valueLocation(ValueLocation.RESPONSE_BODY).valueSelector("NAMEA").build();

        Field model2Field = Field.builder().name("Value").type(FieldType.OBJECT).subfields(
                List.of(
                        Field.builder().name("IndividualCustomer").type(FieldType.OBJECT).valueLocation(ValueLocation.RESPONSE_BODY).subfields(
                                        List.of(
                                                Field.builder().name("FirstName").type(FieldType.BASIC).valueLocation(ValueLocation.RESPONSE_BODY).valueSelector("NAMEA").build()
                                        )
                                )
                                .build()
                )
        ).build();
        Model model2 = Model.builder().type(ModelType.OBJECT).fields(List.of(model2Field)).build();

        String fimpleResponse = JsonBuilder.build(model2, modelData);
        String expectedFimpleResponse = """
                {
                    "Value": {
                        "IndividualCustomer": {
                            "FirstName": "NAZİFE"
                        }
                    }
                }
                """;

        JSONAssert.assertEquals(expectedFimpleResponse, fimpleResponse, true);
    }

    private ModelData createExampleModelData() {
        ModelData modelData = new ModelData();
        Map<String, String> pathVariables = new HashMap<>();
        pathVariables.put("IdentificationNumber", "21498572558");
        modelData.setPathVariables(pathVariables);
        modelData.setResponseBody(getExampleResponseBody());

        return modelData;
    }

    private String getExampleResponseBody() {
        return """
                {              
                  "EXPLANATION1": "",     
                  "EXPLANATION2": "",         
                  "NAMEA": "NAZİFE"     
                }
                """;
    }
}
