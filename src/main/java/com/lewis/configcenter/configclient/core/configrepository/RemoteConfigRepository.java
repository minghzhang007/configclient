package com.lewis.configcenter.configclient.core.configrepository;

import com.lewis.configcenter.configcore.model.ConfigDTO;
import com.lewis.configcenter.configclient.constants.HttpStatusEnum;
import com.lewis.configcenter.configclient.core.concurrent.DefaultThreadFactory;
import com.lewis.configcenter.configclient.inject.ConfigInjector;
import com.lewis.configcenter.configclient.utils.http.HttpRequest;
import com.lewis.configcenter.configclient.utils.http.HttpResponse;
import com.lewis.configcenter.configclient.utils.http.HttpUtil;


import javax.inject.Inject;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lewis0077
 */
public class RemoteConfigRepository extends AbstractConfigRepository {

    private String appId;

    private volatile AtomicReference<ConfigDTO> configCache;

    private static final ScheduledExecutorService executorService;

    private HttpUtil httpUtil;

    @Inject
    public RemoteConfigRepository(String appId) {
        this.appId = appId;
        this.configCache = new AtomicReference<>();
        httpUtil = ConfigInjector.getInstance(HttpUtil.class);
    }

    static {
        //todo 再优化
        executorService = Executors.newScheduledThreadPool(1, new DefaultThreadFactory("RemoteConfigRepository"));
    }

    @Override
    protected void sync() {
        ConfigDTO previousConfig = configCache.get();
        ConfigDTO currentConfig = loadConfig();
        if (previousConfig != currentConfig) {
            configCache.set(currentConfig);
            this.fireRepositoryChange(appId, this.getConfig());
        }
    }

    private ConfigDTO loadConfig() {
        String url = assembleUrl();
        HttpRequest httpRequest = new HttpRequest(url);
        HttpResponse<ConfigDTO> response = httpUtil.get(httpRequest, ConfigDTO.class);
        if (response.getStatusCode() == HttpStatusEnum.SC_NOT_MODIFIED.getStatusCode()) {
            return configCache.get();
        }
        ConfigDTO result = response.getBody();
        return result;
    }

    private String assembleUrl() {
        return "http://localhost:8010/config/snailreader-finance";
    }

    @Override
    public Properties getConfig() {
        if (configCache.get() == null) {
            sync();
        }
        ConfigDTO configDTO = configCache.get();
        Properties properties = new Properties();
        properties.putAll(configDTO.getConfigurations());
        return properties;
    }

    @Override
    public void setUpstreamConfigRepository(ConfigRepository upstreamConfigRepository) {

    }
}
