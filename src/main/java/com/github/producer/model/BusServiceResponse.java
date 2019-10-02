package com.github.producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BusServiceResponse {

    @JsonProperty(value="BusStopCode")
    private String BusStopCode;

    @JsonProperty(value="Services")
    private List<Service> Services;

    public String getBusStopCode() {
        return BusStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        BusStopCode = busStopCode;
    }

    public List<Service> getServices() {
        return Services;
    }

    public void setServices(List<Service> services) {
        Services = services;
    }
}
