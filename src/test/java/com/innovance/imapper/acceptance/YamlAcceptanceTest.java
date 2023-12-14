package com.innovance.imapper.acceptance;

import com.innovance.imapper.acceptance.model.ServiceMapping;
import com.innovance.imapper.acceptance.model.ServiceMappingsDto;
import com.innovance.imapper.jsonbuilder.JsonBuilder;
import com.innovance.imapper.jsonbuilder.model.ModelData;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class YamlAcceptanceTest {

    @Test
    void test() {
        ServiceMappingsDto serviceMappingsDto = readTestYaml();
        ServiceMapping serviceMapping = serviceMappingsDto.getServiceMappingList().get(0);
        ModelData modelData = createExampleModelData();

        String backendRequest = JsonBuilder.build(serviceMapping.getRequestModel(), modelData);
        String expectedBackendRequest = """
                {
                    "SEARCHTITLE": "21498572558"
                }
                """;

        String fimpleResponse = JsonBuilder.build(serviceMapping.getResponseModel(), modelData);
        String expectedFimpleResponse = """
                {
                    "Value": {
                        "IndividualCustomer": {
                            "FirstName": "NAZİFE"
                        }
                    }
                }
                """;

        JSONAssert.assertEquals(backendRequest, expectedBackendRequest, true);
        JSONAssert.assertEquals(fimpleResponse, expectedFimpleResponse, true);
    }

    private ServiceMappingsDto readTestYaml() {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("service-mapping.yml");

        Yaml yaml = new Yaml(new Constructor(ServiceMappingsDto.class, new LoaderOptions()));

        return yaml.load(inputStream);
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
