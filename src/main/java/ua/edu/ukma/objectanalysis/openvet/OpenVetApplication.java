package ua.edu.ukma.objectanalysis.openvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ua.edu.ukma.objectanalysis.openvet.security.JwtProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
public class OpenVetApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenVetApplication.class, args);
    }

}
