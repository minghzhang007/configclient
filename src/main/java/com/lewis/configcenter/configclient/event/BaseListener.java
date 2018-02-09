package com.lewis.configcenter.configclient.event;

import java.util.EventListener;

public interface BaseListener<E extends BaseEvent> extends EventListener {

    boolean supportEvent(BaseEvent baseEvent);

    void onEvent(E event);

}
