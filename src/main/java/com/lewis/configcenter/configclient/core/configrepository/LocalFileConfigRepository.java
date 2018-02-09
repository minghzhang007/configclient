package com.lewis.configcenter.configclient.core.configrepository;

import com.google.common.base.Strings;
import com.lewis.configcenter.configclient.event.BaseEvent;
import com.lewis.configcenter.configclient.event.ConfigChangeEvent;
import com.lewis.configcenter.configclient.event.ConfigChangeListener;
import com.lewis.configcenter.configclient.event.RepositoryChangeListener;
import com.lewis.configcenter.configclient.inject.ConfigInjector;
import com.lewis.configcenter.configclient.utils.ConfigUtil;


import java.io.File;
import java.util.Properties;

/**
 * @author lewis0077
 */
public class LocalFileConfigRepository extends AbstractConfigRepository implements ConfigChangeListener {

    private String appId;

    private File baseFile;

    private volatile Properties fileProperties;

    private volatile ConfigRepository upstreamConfigRepository;

    private ConfigUtil configUtil;

    public LocalFileConfigRepository(String appId, ConfigRepository configRepository) {
        this.appId = appId;
        this.configUtil = ConfigInjector.getInstance(ConfigUtil.class);
        this.setLocalCacheDir(findLocalCacheDir(), false);
        this.trySync();
    }

    private File findLocalCacheDir() {
        String baseFileDir = System.getProperty("baseFileDir");
        if (Strings.isNullOrEmpty(baseFileDir)) {
            baseFileDir = configUtil.getDefaultBaseFileDir();
        }

        return null;
    }

    private void setLocalCacheDir(File baseFile, boolean syncRightNow) {

    }


    @Override
    protected boolean trySync() {
        return super.trySync();
    }

    @Override
    protected void sync() {

    }

    @Override
    public Properties getConfig() {
        return null;
    }

    @Override
    public void addChangeListener(RepositoryChangeListener listener) {
        super.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(RepositoryChangeListener listener) {
        super.removeChangeListener(listener);
    }

    @Override
    protected void fireRepositoryChange(String appId, Properties newProperties) {
        super.fireRepositoryChange(appId, newProperties);
    }

    @Override
    public boolean supportEvent(BaseEvent baseEvent) {
        return baseEvent instanceof ConfigChangeListener;
    }

    @Override
    public void onEvent(ConfigChangeEvent event) {

    }
}
