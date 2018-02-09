package com.lewis.configcenter.configclient.internal;

import com.google.common.collect.Maps;
import com.lewis.configcenter.configclient.internal.spi.Provider;

import java.util.concurrent.ConcurrentMap;

/**
 * @author lewis0077
 */
public class DefaultProviderManager implements ProviderManager {

    private ConcurrentMap<Class<? extends Provider>, Provider> providerMap = Maps.newConcurrentMap();

    public DefaultProviderManager() {
        Provider appProvider = new DefaultAppProvider();
        appProvider.init();
        register(appProvider);
    }

    private void register(Provider appProvider) {
        providerMap.putIfAbsent(appProvider.getType(), appProvider);
    }


    @Override
    public <T extends Provider> T getProvider(Class<T> clazz) {
        return null;
    }
}
