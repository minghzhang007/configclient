package com.lewis.configcenter.configclient.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lewis.configcenter.configclient.core.config.Config;
import com.lewis.configcenter.configclient.core.config.DefaultConfig;
import com.lewis.configcenter.configclient.core.configrepository.ConfigRepository;
import com.lewis.configcenter.configclient.core.configrepository.RemoteConfigRepository;


/**
 * @author zhangminghua
 */
public class ConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).to(DefaultConfig.class);
        bind(ConfigRepository.class).to(RemoteConfigRepository.class);
    }

    @Provides
    public String provider(){
        return "snailreader-finance";
    }
}
