package com.example.restservice.data;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("prop.properties")
@Data
public class AppProperties {

    @Value("${name}")
    private String name;

}
