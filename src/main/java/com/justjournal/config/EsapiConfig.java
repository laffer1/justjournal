package com.justjournal.config;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsapiConfig {

    @Bean
    public Encoder encoder() {
        return ESAPI.encoder();
    }
}