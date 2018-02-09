package com.lewis.configcenter.configclient.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangminghua
 */
@Data
@NoArgsConstructor
public class ConfigChange {

    private String appId;

    private String propertyName;

    private String newValue;

    private String oldValue;

    private ChangeTypeEnum changeTypeEnum;

    public ConfigChange(String appId, String propertyName, String newValue, String oldValue, ChangeTypeEnum changeTypeEnum) {
        this.appId = appId;
        this.propertyName = propertyName;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.changeTypeEnum = changeTypeEnum;
    }
}
