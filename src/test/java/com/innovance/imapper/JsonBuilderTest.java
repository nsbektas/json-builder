package com.innovance.imapper;

import com.innovance.imapper.jsonbuilder.JsonBuilder;
import com.innovance.imapper.jsonbuilder.model.Model;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import com.innovance.imapper.jsonbuilder.model.ModelType;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.jupiter.api.Assertions.assertNull;

class JsonBuilderTest {

    @Test
    void givenNullModelType_shouldBuildNull() {
        Model model = Model.builder().type(ModelType.NULL).build();
        ModelData modelData = new ModelData();

        String output = new JsonBuilder(model, modelData).build();

        assertNull(output);
    }

    @Test
    void givenEmptyStringModelType_shouldBuildSuccessfully() {
        Model model = Model.builder().type(ModelType.EMPTY_STRING).build();
        ModelData modelData = new ModelData();

        String output = new JsonBuilder(model, modelData).build();

        JSONAssert.assertEquals("", output, true);
    }

    @Test
    void givenEmptyObjectModelType_shouldBuildSuccessfully() {
        Model model = Model.builder().type(ModelType.EMPTY_OBJECT).build();
        ModelData modelData = new ModelData();

        String output = new JsonBuilder(model, modelData).build();

        JSONAssert.assertEquals("{}", output, true);
    }

    @Test
    void givenEmptyListModelType_shouldBuildSuccessfully() {
        Model model = Model.builder().type(ModelType.EMPTY_LIST).build();
        ModelData modelData = new ModelData();

        String output = new JsonBuilder(model, modelData).build();

        JSONAssert.assertEquals("[]", output, true);
    }
}
