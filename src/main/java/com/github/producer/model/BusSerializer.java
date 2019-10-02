package com.github.producer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class BusSerializer implements Serializer<Bus> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String s, Bus bus) {
        byte[] retVal = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            retVal = objectMapper.writeValueAsString(bus).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public void close() {

    }
}
