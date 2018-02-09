package com.lewis.configcenter.configclient.internal.spi;

import java.io.InputStream;

public interface AppProvider extends Provider{

    String getAppId();

    void init(InputStream inputStream);
}
