package com.lewis.configcenter.configclient.core.config;

import com.google.common.collect.Maps;
import com.lewis.configcenter.configclient.core.configrepository.ConfigRepository;
import com.lewis.configcenter.configclient.event.BaseEvent;
import com.lewis.configcenter.configclient.event.ConfigChangeEvent;
import com.lewis.configcenter.configclient.event.RepositoryChangeEvent;
import com.lewis.configcenter.configclient.event.RepositoryChangeListener;
import com.lewis.configcenter.configclient.model.ConfigChange;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhangminghua
 */
public class DefaultConfig extends AbstractConfig implements RepositoryChangeListener {
    private Logger logger = LoggerFactory.getLogger(DefaultConfig.class);
    private AtomicReference<Properties> configProperties;

    private Properties resourceProperties;

    private ConfigRepository configRepository;

    private String appId;

    public DefaultConfig(String appId, ConfigRepository configRepository) {
        this.appId = appId;
        this.configProperties = new AtomicReference<Properties>();
        this.configRepository = configRepository;
        init();
    }

    public DefaultConfig() {
    }

    private void init() {
        try {
            Properties config = configRepository.getConfig();
            configProperties.set(config);
        } catch (Exception ex) {


        } finally {
            configRepository.addChangeListener(this);
        }

    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        if (value == null && configProperties.get() != null) {
            return configProperties.get().getProperty(key);
        }

        if (value == null) {
            value = System.getenv(key);
        }

        if (value == null && resourceProperties != null) {
            value = resourceProperties.getProperty(key);
        }
        if (value == null && configProperties.get() == null) {
            logger.warn("Could not load config for appId {} from ConfigCenter, please check whether the configs are released in ConfigCenter! Return default value now!", appId);
        }

        return defaultValue;
    }

    @Override
    public Set<String> getPropertyNames() {
        return null;
    }

    @Override
    public boolean supportEvent(BaseEvent baseEvent) {
        return baseEvent instanceof RepositoryChangeEvent;
    }

    @Override
    public void onEvent(RepositoryChangeEvent repositoryChangeEvent) {
        String appId = repositoryChangeEvent.getAppId();
        Properties newProperties = repositoryChangeEvent.getNewProperties();
        if (newProperties.equals(configProperties.get())) {
            return;
        }
        Properties newConfigProperties = new Properties();
        newConfigProperties.putAll(newProperties);

        Map<String, ConfigChange> configChangeMap = calcConfigChanges(newConfigProperties);
        if (MapUtils.isEmpty(configChangeMap)) {
            return;
        }
        this.fireConfigChangeEvent(new ConfigChangeEvent(this, appId, configChangeMap));
    }

    private Map<String, ConfigChange> calcConfigChanges(Properties newConfigProperties) {

        List<ConfigChange> configChanges = propertyChanges(appId, configProperties.get(), newConfigProperties);
        Map<String, ConfigChange> map = Maps.newHashMapWithExpectedSize(configChanges.size());
        for (ConfigChange configChange : configChanges) {
            map.put(configChange.getPropertyName(), configChange);
        }
        return map;
    }
}
