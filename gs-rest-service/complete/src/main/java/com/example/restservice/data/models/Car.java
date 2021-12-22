package com.example.restservice.data.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Car {

    public enum CarType {
        LIGHT,
        HEAVY,
        UNKNOWN,
        CUSTOM
    }


    @Builder.Default
    private String number = "Default_Number_123";
    @Builder.Default
    private String model = "Default_model";
    @Builder.Default
    private CarType carType = CarType.UNKNOWN;

}



