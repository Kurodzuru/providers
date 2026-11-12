package org.example.providers;

import org.example.providers.dto.Provider;
import org.example.providers.exceptions.UnknownProviderException;
import org.example.providers.provider.ProviderSource;
import org.springframework.web.client.RestClient;

import java.util.*;

public class ProviderRegistry {
    private final RestClient.Builder restClientBuilder;
    private volatile Map<String, ProviderClient> clients = Map.of();

    public ProviderRegistry(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = Objects.requireNonNull(restClientBuilder, "restClientBuilder");
    }

    public void replaceAll(Collection<Provider> providers) {
        Objects.requireNonNull(providers, "providers");
        Map<String, ProviderClient> next = new LinkedHashMap<>();

        for (Provider provider : providers) {
            Objects.requireNonNull(provider, "provider");
            if (next.containsKey(provider.id())) {
                throw new IllegalArgumentException("Duplicate provider: " + provider.id());
            }
            next.put(provider.id(), new ProviderClient(provider, restClientBuilder));
        }
        clients = Map.copyOf(next);
    }

    public void load(ProviderSource source) {
        replaceAll(source.loadProviders());
    }

    public boolean isAvailable(String providerId, String phone) {
        Objects.requireNonNull(phone, "phone");
        ProviderClient client = clients.get(providerId);

        if (client == null) {
            throw new UnknownProviderException(providerId);
        }
        return client.isAvailable(phone);
    }

    public Set<String> getProviderIds() {
        return clients.keySet();
    }
}
