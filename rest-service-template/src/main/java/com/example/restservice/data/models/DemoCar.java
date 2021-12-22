package com.example.restservice.data.models;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DemoCar extends Car {

    public DemoCar(
            @Value("${demo.car.number}") String number,
            @Value("${demo.car.model}") String model,
            @Value("${demo.car.type}")CarType type) {

        setNumber(number);
        setModel(model);
        setCarType(type);

    }

}
