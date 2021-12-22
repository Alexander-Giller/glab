package com.example.restservice.data;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("./prop.properties")
@ConfigurationProperties(prefix = "personal")
@Data
public class PersonalData {

    private String description;
    private String nickname;

}
