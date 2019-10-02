package com.github.producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Service {

    @JsonProperty(value="ServiceNo")
    private String ServiceNo;

    @JsonProperty(value="Operator")
    private String Operator;

    @JsonProperty(value="NextBus")
    private Bus NextBus;

    @JsonProperty(value="NextBus2")
    private Bus NextBus2;

    @JsonProperty(value="NextBus3")
    private Bus NextBus3;

    public String getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(String serviceNo) {
        ServiceNo = serviceNo;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public Bus getNextBus() {
        return NextBus;
    }

    public void setNextBus(Bus nextBus) {
        NextBus = nextBus;
    }

    public Bus getNextBus2() {
        return NextBus2;
    }

    public void setNextBus2(Bus nextBus2) {
        NextBus2 = nextBus2;
    }

    public Bus getNextBus3() {
        return NextBus3;
    }

    public void setNextBus3(Bus nextBus3) {
        NextBus3 = nextBus3;
    }
}
