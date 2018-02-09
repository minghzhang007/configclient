package com.lewis.configcenter.configclient.core.config;

import com.lewis.configcenter.configclient.event.ConfigChangeListener;

import java.util.Set;

public interface Config {

    String getProperty(String key, String defaultValue);

    Set<String> getPropertyNames();

    void addChangeListener(ConfigChangeListener listener);

    Integer getIntProperty(String key, Integer defaultValue);


    Long getLongProperty(String key, Long defaultValue);


    Short getShortProperty(String key, Short defaultValue);


    Float getFloatProperty(String key, Float defaultValue);


    Double getDoubleProperty(String key, Double defaultValue);


    Byte getByteProperty(String key, Byte defaultValue);


    Boolean getBooleanProperty(String key, Boolean defaultValue);


    String[] getArrayProperty(String key, String delimiter, String[] defaultValue);
}
