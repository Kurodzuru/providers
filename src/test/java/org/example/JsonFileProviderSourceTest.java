package org.example;

import org.example.dto.Provider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonFileProviderSourceTest {
    @TempDir
    Path dir;

    @Test
    void readsProvidersFromFile() throws Exception {
        Path file = dir.resolve("providers.json");
        Files.writeString(file, """
                [
                  {"id": "alpha", "baseUrl": "https://alpha.example.com", "apiKey": "key-a"},
                  {"id": "beta",  "baseUrl": "https://beta.example.com/", "apiKey": "key-b"}
                ]
                """);

        List<Provider> providers = new ProviderSourceJson(file).loadProviders();

        assertEquals(List.of(
                new Provider("alpha", "https://alpha.example.com", "key-a"),
                new Provider("beta", "https://beta.example.com", "key-b")), providers);
    }
}
