package com.example.restservice.data;


import com.example.restservice.data.models.Car;
import com.example.restservice.data.models.Person;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Data
@Component
public class Factory {

    @Bean(name = "defaultPerson")
    public Person createPerson() {
        return new Person();
    }

    @Bean(name = "defaultCar")
    public Car createCar() {
        return new Car();
    }

    @Bean(name = "customCar")
    public Car createCar(
            @Value("${car.custom.number:default_number}") String number,
            @Value("${car.custom.model:default_model}") String model,
            @Value("${car.custom.carType:UNKNOWN}") Car.CarType carType) {
        return new Car(number, model, carType);
    }

    @Bean(name = "customPerson")
    public Person createPerson(
            @Value("${person.custom.name:default_name}") String name,
            @Value("${person.custom.surname:default_surname}") String surname) {
        return new Person(name, surname);
    }

}
