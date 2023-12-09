package com.innovance.imapper.controller;

import com.innovance.imapper.controller.dto.RequestDto;
import com.innovance.imapper.mapper.ObjectBuilder;
import com.innovance.imapper.mapper.model.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapperController {

    @PostMapping("build-request")
    public ResponseEntity<String> buildRequest(@RequestBody RequestDto requestDto) {
        Request request = new Request(requestDto.getPathVariables(), requestDto.getQueryParameters(), requestDto.getBody());
        ObjectBuilder objectBuilder = new ObjectBuilder(requestDto.getFieldMappings());
        String output = objectBuilder.buildJson(request);

        return ResponseEntity.ok(output);
    }
}
