package com.lewis.configcenter.configclient.event;

import lombok.Data;

import java.util.EventObject;

/**
 * @author zhangminghua
 */
@Data
public class BaseEvent extends EventObject {

    private long timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public BaseEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
}
