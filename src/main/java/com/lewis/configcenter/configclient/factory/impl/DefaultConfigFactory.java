package com.lewis.configcenter.configclient.factory.impl;

import com.lewis.configcenter.configclient.core.config.Config;
import com.lewis.configcenter.configclient.core.config.DefaultConfig;
import com.lewis.configcenter.configclient.factory.ConfigFactory;
import com.lewis.configcenter.configclient.factory.ConfigRepositoryEnum;
import com.lewis.configcenter.configclient.factory.ConfigRepositoryFactory;
import com.lewis.configcenter.configclient.inject.ConfigInjector;

/**
 * @author lewis0077
 */
public class DefaultConfigFactory implements ConfigFactory {

    private ConfigRepositoryFactory repositoryFactory;

    public DefaultConfigFactory() {
        repositoryFactory = ConfigInjector.getInstance(ConfigRepositoryFactory.class);
    }

    @Override
    public Config createConfig(String appId) {
        return new DefaultConfig(appId, repositoryFactory.newInstance(appId, ConfigRepositoryEnum.LOCAL_FILE));
    }
}
