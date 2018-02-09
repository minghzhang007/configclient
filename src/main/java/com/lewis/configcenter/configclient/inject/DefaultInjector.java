package com.lewis.configcenter.configclient.inject;

import com.google.inject.Guice;

/**
 * @author zhangminghua
 */
public class DefaultInjector implements Injector {

    private com.google.inject.Injector injector;

    public DefaultInjector() {
        injector = Guice.createInjector(new ConfigModule());
    }



    @Override
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

}
