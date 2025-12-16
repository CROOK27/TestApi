package ru.rtc.medline.application.infrastructure.common.actuator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ActuatorConfiguration {

    private static final String CONFIGURATION_SANITIZER_KEY = "management.endpoint.env.keys-to-sanitize";

    @Bean
    @ConfigurationProperties(prefix = CONFIGURATION_SANITIZER_KEY)
    public List<String> keysToSanitize() {
        return new ArrayList<>();
    }

}

