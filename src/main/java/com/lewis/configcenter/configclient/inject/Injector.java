package com.lewis.configcenter.configclient.inject;

/**
 * @author zhangminghua
 */
public interface Injector {

    <T> T getInstance(Class<T> type);
}
