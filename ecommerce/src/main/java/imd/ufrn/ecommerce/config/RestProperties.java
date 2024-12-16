package imd.ufrn.ecommerce.config;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
// @ConfigurationProperties(value = "spring.backend")
public class RestProperties {

    private String host = "http://localhost:8080";

}