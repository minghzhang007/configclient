package com.lewis.configcenter.configclient.factory.impl;

import com.lewis.configcenter.configclient.core.configrepository.ConfigRepository;
import com.lewis.configcenter.configclient.core.configrepository.LocalFileConfigRepository;
import com.lewis.configcenter.configclient.core.configrepository.RemoteConfigRepository;
import com.lewis.configcenter.configclient.factory.ConfigRepositoryEnum;
import com.lewis.configcenter.configclient.factory.ConfigRepositoryFactory;

/**
 * @author lewis0077
 */
public class DefaultConfigRepositoryFactory implements ConfigRepositoryFactory {

    @Override
    public ConfigRepository newInstance(String appId, ConfigRepositoryEnum configRepositoryEnum) {
        switch (configRepositoryEnum) {
            case LOCAL_FILE:
                return new LocalFileConfigRepository(appId, newInstance(appId, ConfigRepositoryEnum.REMOTE));
            case REMOTE:
                return new RemoteConfigRepository(appId);
            default:
                throw new IllegalArgumentException("参数错误");
        }
    }

}
