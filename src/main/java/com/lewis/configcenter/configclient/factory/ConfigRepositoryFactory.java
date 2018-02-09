package com.lewis.configcenter.configclient.factory;

import com.lewis.configcenter.configclient.core.configrepository.ConfigRepository;

public interface ConfigRepositoryFactory {

    ConfigRepository newInstance(String appId, ConfigRepositoryEnum configRepositoryEnum);
}
