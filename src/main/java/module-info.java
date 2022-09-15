module MyProjectAPI {
    requires com.google.gson;
    requires spark.core;
    opens com.demoapp.api.model;
    opens com.demoapp.api.services;
    opens com.demoapp.api.services.http;
}