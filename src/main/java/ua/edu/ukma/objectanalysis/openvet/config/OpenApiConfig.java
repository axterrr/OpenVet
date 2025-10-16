package ua.edu.ukma.objectanalysis.openvet.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "OpenVet API",
        version = "0.1.0",
        description = "REST API for veterinary clinic management"
    )
)
public class OpenApiConfig { }

