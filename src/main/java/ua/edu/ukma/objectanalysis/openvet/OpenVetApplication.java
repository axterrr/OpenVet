package ua.edu.ukma.objectanalysis.openvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.edu.ukma.objectanalysis.openvet.security.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class OpenVetApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenVetApplication.class, args);
    }

}
