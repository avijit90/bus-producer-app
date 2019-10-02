package com.github.producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bus {

    @JsonProperty(value = "OriginCode")
    private String OriginCode;

    @JsonProperty(value = "DestinationCode")
    private String DestinationCode;

    @JsonProperty(value = "EstimatedArrival")
    private String EstimatedArrival;

    @JsonProperty(value = "Latitude")
    private String Latitude;

    @JsonProperty(value = "Longitude")
    private String Longitude;

    @JsonProperty(value = "VisitNumber")
    private String VisitNumber;

    @JsonProperty(value = "Load")
    private String Load;

    @JsonProperty(value = "Feature")
    private String Feature;

    @JsonProperty(value = "Type")
    private String Type;

    public String getOriginCode() {
        return OriginCode;
    }

    public void setOriginCode(String originCode) {
        OriginCode = originCode;
    }

    public String getDestinationCode() {
        return DestinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        DestinationCode = destinationCode;
    }

    public String getEstimatedArrival() {
        return EstimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        EstimatedArrival = estimatedArrival;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getVisitNumber() {
        return VisitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        VisitNumber = visitNumber;
    }

    public String getLoad() {
        return Load;
    }

    public void setLoad(String load) {
        Load = load;
    }

    public String getFeature() {
        return Feature;
    }

    public void setFeature(String feature) {
        Feature = feature;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
