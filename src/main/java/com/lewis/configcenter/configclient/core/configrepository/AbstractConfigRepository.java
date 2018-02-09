package com.lewis.configcenter.configclient.core.configrepository;

import com.google.common.collect.Lists;
import com.lewis.configcenter.configclient.event.RepositoryChangeEvent;
import com.lewis.configcenter.configclient.event.RepositoryChangeListener;

import java.util.List;
import java.util.Properties;

/**
 * @author lewis0077
 */
public abstract class AbstractConfigRepository implements ConfigRepository {

    private List<RepositoryChangeListener> repositoryChangeListeners = Lists.newCopyOnWriteArrayList();

    protected boolean trySync() {
        try {
            sync();
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

    protected abstract void sync();

    @Override
    public void addChangeListener(RepositoryChangeListener listener) {
        if (!repositoryChangeListeners.contains(listener)) {
            repositoryChangeListeners.add(listener);
        }
    }

    @Override
    public void removeChangeListener(RepositoryChangeListener listener) {
        repositoryChangeListeners.remove(listener);
    }

    protected void fireRepositoryChange(String appId, Properties newProperties) {
        for (RepositoryChangeListener repositoryChangeListener : repositoryChangeListeners) {
            try {
                repositoryChangeListener.onEvent(new RepositoryChangeEvent(this, appId, newProperties));
            } catch (Exception ex) {

            }
        }
    }
}
