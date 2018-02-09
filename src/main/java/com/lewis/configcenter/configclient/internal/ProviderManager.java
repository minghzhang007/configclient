package com.lewis.configcenter.configclient.internal;

import com.lewis.configcenter.configclient.internal.spi.Provider;

/**
 * @author lewis0077
 */
public interface ProviderManager {

    <T extends Provider> T getProvider(Class<T> clazz);
}
