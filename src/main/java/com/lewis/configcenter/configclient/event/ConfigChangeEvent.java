package com.lewis.configcenter.configclient.event;

import com.lewis.configcenter.configclient.model.ConfigChange;
import lombok.Data;

import java.util.Map;

/**
 * @author zhangminghua
 */
@Data
public class ConfigChangeEvent extends BaseEvent {

    private String appId;

    private Map<String, ConfigChange> changes;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ConfigChangeEvent(Object source, String appId, Map<String, ConfigChange> changes) {
        super(source);
        this.appId = appId;
        this.changes = changes;
    }
}
