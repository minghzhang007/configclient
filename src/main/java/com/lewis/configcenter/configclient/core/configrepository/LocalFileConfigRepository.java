package com.lewis.configcenter.configclient.core.configrepository;

import com.google.common.base.Strings;
import com.lewis.configcenter.configclient.event.BaseEvent;
import com.lewis.configcenter.configclient.event.RepositoryChangeEvent;
import com.lewis.configcenter.configclient.event.RepositoryChangeListener;
import com.lewis.configcenter.configclient.inject.ConfigInjector;
import com.lewis.configcenter.configclient.utils.ConfigUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author lewis0077
 */
public class LocalFileConfigRepository extends AbstractConfigRepository implements RepositoryChangeListener {

    private String appId;

    private File baseDir;

    private volatile Properties fileProperties;

    private volatile ConfigRepository upstreamConfigRepository;

    private static final String CONFIG_DIR = "config-cache";

    private ConfigUtil configUtil;

    public LocalFileConfigRepository(String appId, ConfigRepository configRepository) {
        this.appId = appId;
        this.configUtil = ConfigInjector.getInstance(ConfigUtil.class);
        this.setLocalCacheDir(findLocalCacheDir(), false);
        this.setUpstreamConfigRepository(configRepository);
        this.trySync();
    }

    private File findLocalCacheDir() {
        String baseFileDir = System.getProperty("baseFileDir");
        if (Strings.isNullOrEmpty(baseFileDir)) {
            baseFileDir = configUtil.getDefaultBaseFileDir();
        }
        Path path = Paths.get(baseFileDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.exists(path) && Files.isWritable(path)) {
            return new File(baseFileDir, CONFIG_DIR);
        }

        return null;
    }

    private void setLocalCacheDir(File baseFile, boolean syncRightNow) {
        this.baseDir = baseFile;
        checkLocalFileCacheDir(baseFile);
        if (syncRightNow) {
            this.trySync();
        }
    }

    private void checkLocalFileCacheDir(File baseFile) {
        if (baseFile.exists()) {
            return;
        }
        try {
            Files.createDirectory(baseFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void sync() {
        boolean syncFromUpstreamResult = syncFromUpstreamConfigRepository();
        if (syncFromUpstreamResult) {
            return;
        }

        fileProperties = loadCacheFile();
    }

    private Properties loadCacheFile() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(baseDir);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private boolean syncFromUpstreamConfigRepository() {
        if (upstreamConfigRepository == null) {
            return false;
        }
        Properties remoteConfig = upstreamConfigRepository.getConfig();
        updateLocalCacheFile(remoteConfig);
        return true;
    }

    private synchronized void updateLocalCacheFile(Properties remoteConfig) {
        if (remoteConfig.equals(fileProperties)) {
            return;
        }
        persistToCacheFile(remoteConfig);
    }

    private void persistToCacheFile(Properties remoteConfig) {
        try {
            File cacheFile = getCacheFile(baseDir, appId);
            OutputStream outputStream = new FileOutputStream(cacheFile);
            remoteConfig.store(outputStream, "persistToCacheFile");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getCacheFile(File baseDir, String appId) {
        String fileName = String.format("%s.properties", appId);
        return new File(baseDir, fileName);
    }

    @Override
    public Properties getConfig() {
        if (fileProperties == null) {
            sync();
        }
        Properties properties = new Properties();
        properties.putAll(fileProperties);
        return properties;
    }

    @Override
    public void setUpstreamConfigRepository(ConfigRepository upstreamConfigRepository) {
        if (upstreamConfigRepository == null) {
            return;
        }
        if (this.upstreamConfigRepository != null) {
            this.upstreamConfigRepository.removeChangeListener(this);
        }
        this.upstreamConfigRepository = upstreamConfigRepository;
        syncFromUpstreamConfigRepository();
        upstreamConfigRepository.addChangeListener(this);
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
        return baseEvent instanceof RepositoryChangeEvent;
    }

    @Override
    public void onEvent(RepositoryChangeEvent event) {

    }


}
