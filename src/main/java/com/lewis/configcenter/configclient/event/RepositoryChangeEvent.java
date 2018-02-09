package com.lewis.configcenter.configclient.event;

import lombok.Data;

import java.util.Properties;

@Data
public class RepositoryChangeEvent extends BaseEvent {

    private String appId;

    private Properties newProperties;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RepositoryChangeEvent(Object source, String appId,Properties newProperties) {
        super(source);
        this.appId = appId;
        this.newProperties = newProperties;
    }
}
