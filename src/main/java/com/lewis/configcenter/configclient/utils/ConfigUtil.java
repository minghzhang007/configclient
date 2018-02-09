package com.lewis.configcenter.configclient.utils;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * @author lewis0077
 */
@Data
public class ConfigUtil {

    private int connectTimeout = 1000;

    private int readTimeout = 5000;

    public String getDefaultBaseFileDir() {
        boolean osWindows = isOsWindows();
        String baseFileDir = osWindows ? "C:\\opt\\data\\%s" : "/opt/data/%ss";
        return baseFileDir;
    }

    private boolean isOsWindows() {
        String osName = System.getProperty("os.name");
        if (Strings.isNullOrEmpty(osName)) {
            return false;
        }
        return osName.startsWith("Windows");
    }
}
