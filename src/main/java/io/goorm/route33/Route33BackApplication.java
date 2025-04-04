package io.goorm.route33;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@ConfigurationPropertiesScan
@SpringBootApplication
public class Route33BackApplication {

    public static void main(String[] args) {
        SpringApplication.run(Route33BackApplication.class, args);
    }

}
