package com.innovance.imapper.mapper.model.entity;

import com.innovance.imapper.jsonbuilder.model.Model;

//TODO will be implemented later.
public class Service {
    long id;
    String path; // Original Request Path to determine which backend service will be called.
    String httpMethod; // identifier httpMethod + Path olacak muhtemelen.
    String backendServiceNo; // backend service identifier.

    Model requestModel; //Backend(colendi) service model
    Model responseModel; //Fimple response model
}