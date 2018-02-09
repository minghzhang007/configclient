package com.lewis.configcenter.configclient.internal;

import com.lewis.configcenter.configclient.internal.spi.AppProvider;
import com.lewis.configcenter.configclient.internal.spi.Provider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lewis0077
 */
public class DefaultAppProvider implements AppProvider {

    private static final String APP_PROPERTIES_LOCATION = "/META-INF/app.properties";

    private Properties appProperties = new Properties();

    private String appId;

    @Override
    public String getAppId() {
        return appId;
    }


    @Override
    public void init() {
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES_LOCATION);
            if (inputStream == null) {
                inputStream = DefaultAppProvider.class.getResourceAsStream(APP_PROPERTIES_LOCATION);
            }
            init(inputStream);
        } catch (Exception ex) {

        }
    }

    @Override
    public void init(InputStream inputStream) {
        if (inputStream != null) {
            try {
                appProperties.load(inputStream);
                initAppId();
            } catch (Exception ex) {

            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initAppId() {
        String appId = System.getProperty("appId");
        if (StringUtils.isNotBlank(appId)) {
            this.appId = appId.trim();
            return;
        }

        appId = appProperties.getProperty("appId");
        if (StringUtils.isNotBlank(appId)) {
            this.appId = appId.trim();
            return;
        }

    }

    @Override
    public Class<? extends Provider> getType() {
        return DefaultAppProvider.class;
    }

}
