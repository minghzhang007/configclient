package com.lewis.configcenter.configclient.internal.spi;

public interface Provider {

    Class<? extends Provider> getType();

    void init();
}
