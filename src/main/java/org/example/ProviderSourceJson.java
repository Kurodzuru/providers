package org.example;

import org.example.dto.Provider;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProviderSourceJson implements ProviderSource {
    private static final TypeReference<List<Provider>> PROVIDER_LIST = new TypeReference<>() {};

    private final Path filePath;
    private final ObjectMapper mapper;

    public ProviderSourceJson(final Path path) {
        this(path, JsonMapper.builder().build());
    }

    public ProviderSourceJson(final Path path, final ObjectMapper mapper) {
        this.filePath = Objects.requireNonNull(path);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public List<Provider> loadProviders() {
        try {
            List<Provider> providers = mapper.readValue(filePath.toFile(), PROVIDER_LIST);
            return providers == null ? Collections.emptyList() : providers;
        } catch (JacksonException e) {
            throw new IllegalStateException("Cannot read providers from " + filePath, e);
        }
    }
}
