package com.lewis.configcenter.configclient.internal;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author lewis0077
 */
public class ServiceBootstrap {

    public static <T> T loadFirst(Class<T> clazz) {
        Iterator<T> iterator = loadAll(clazz);
        if (!iterator.hasNext()) {
            throw new IllegalStateException(String.format("No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
                    clazz.getName()));
        }
        return iterator.next();
    }

    private static <T> Iterator<T> loadAll(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        return loader.iterator();
    }
}
