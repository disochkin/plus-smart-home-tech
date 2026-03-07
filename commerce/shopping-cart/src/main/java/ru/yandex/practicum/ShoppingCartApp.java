package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@ConfigurationPropertiesScan
@SpringBootApplication
public class ShoppingCartApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ShoppingCartApp.class, args);
    }
}