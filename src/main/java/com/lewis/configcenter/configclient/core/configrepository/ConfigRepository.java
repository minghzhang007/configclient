package com.lewis.configcenter.configclient.core.configrepository;


import com.lewis.configcenter.configclient.event.RepositoryChangeListener;

import java.util.Properties;

public interface ConfigRepository {

    Properties getConfig();

    void setUpstreamConfigRepository(ConfigRepository upstreamConfigRepository);

    void addChangeListener(RepositoryChangeListener listener);

    void removeChangeListener(RepositoryChangeListener listener);
}
