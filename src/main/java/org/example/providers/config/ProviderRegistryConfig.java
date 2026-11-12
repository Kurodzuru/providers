package org.example.providers.config;

import org.example.providers.ProviderRegistry;
import org.example.providers.ProviderSource;
import org.example.providers.ProviderSourceJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;

@Configuration
public class ProviderRegistryConfig {

    @Bean
    public ProviderSource providerSource(@Value("${providers.file}") String file) {
        return new ProviderSourceJson(Path.of(file));
    }

    @Bean
    public ProviderRegistry providerRegistry(RestClient.Builder builder, ProviderSource source) {
        ProviderRegistry registry = new ProviderRegistry(builder);
        registry.load(source);
        return registry;
    }
}