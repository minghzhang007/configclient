package com.lewis.configcenter.configclient.inject;


import com.lewis.configcenter.configclient.internal.ServiceBootstrap;

/**
 * @author lewis0077
 */
public class ConfigInjector {

    private static volatile Injector injector;

    private static Object lock = new Object();

    public static <T> T getInstance(Class<T> clazz) {
        return getInjector().getInstance(clazz);
    }

    public static Injector getInjector() {
        if (injector == null) {
            synchronized (lock) {
                if (injector == null) {
                    injector = ServiceBootstrap.loadFirst(Injector.class);
                }
            }
        }
        return injector;
    }

}
