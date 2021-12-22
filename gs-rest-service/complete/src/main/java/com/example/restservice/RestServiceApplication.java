package com.example.restservice;


import com.example.restservice.data.AppProperties;
import com.example.restservice.data.PersonalData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;


@SpringBootApplication
public class RestServiceApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RestServiceApplication.class, args);

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        System.out.println(ctx.getBean("customCar").toString());
        System.out.println(ctx.getBean("customPerson").toString());
        System.out.println(ctx.getBean("demoCar").toString());
        System.out.println(ctx.getBean("carPark").toString());
        System.out.println(ctx.getBean("appProperties").toString());

        AppProperties appProperties = (AppProperties) ctx.getBean("appProperties");
        System.out.println(appProperties.getName());

        PersonalData personalData = (PersonalData) ctx.getBean("personalData");
        System.out.println(personalData.getDescription());
        System.out.println(personalData.getNickname());

    }

}
