package com.example.restservice.data;


import com.example.restservice.data.models.Car;
import com.example.restservice.data.models.CarPark;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
public class AppConfiguration {

    @Bean(name = "carPark")
    public CarPark getCarPark() {
        Car.CarBuilder carBuilder = Car.builder();
//        return new CarPark();
        return new CarPark(
                Arrays.asList(
                        carBuilder.build(),
                        carBuilder.build(),
                        carBuilder.build()));
    }

}
