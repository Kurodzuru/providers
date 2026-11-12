package org.example.providers.provider;

import org.example.providers.dto.Provider;

import java.util.List;

public interface ProviderSource {
    List<Provider> loadProviders();
}
