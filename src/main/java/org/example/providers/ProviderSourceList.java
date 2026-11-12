package org.example.providers;

import org.example.providers.dto.Provider;

import java.util.List;

public class ProviderSourceList implements ProviderSource{

    private final List<Provider> providers;

    public ProviderSourceList(List<Provider> providers) {
        this.providers = providers;
    }

    @Override
    public List<Provider> loadProviders() {
        return providers;
    }
}
