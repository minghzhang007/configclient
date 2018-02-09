package com.lewis.configcenter.configclient.factory;

import com.lewis.configcenter.configclient.core.config.Config;

/**
 * @author lewis0077
 */
public interface ConfigFactory {

    Config createConfig(String appId);

}
