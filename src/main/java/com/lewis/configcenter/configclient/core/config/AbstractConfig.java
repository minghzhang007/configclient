package com.lewis.configcenter.configclient.core.config;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.lewis.configcenter.configclient.core.concurrent.DefaultThreadFactory;
import com.lewis.configcenter.configclient.event.ConfigChangeEvent;
import com.lewis.configcenter.configclient.event.ConfigChangeListener;
import com.lewis.configcenter.configclient.model.ChangeTypeEnum;
import com.lewis.configcenter.configclient.model.ConfigChange;
import com.lewis.configcenter.configclient.support.TypeTransform;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhangminghua
 */
public abstract class AbstractConfig implements Config {

    private List<ConfigChangeListener> configChangeListeners = Lists.newCopyOnWriteArrayList();

    private static ExecutorService executorService;

    private volatile AtomicLong configVersion;

    private volatile Cache<String, Integer> integerCache;
    private volatile Cache<String, Long> longCache;
    private volatile Cache<String, Short> shortCache;
    private volatile Cache<String, Float> floatCache;
    private volatile Cache<String, Double> doubleCache;
    private volatile Cache<String, Byte> byteCache;
    private volatile Cache<String, Boolean> booleanCache;
    private volatile Cache<String, String[]> arrayCache;


    private List<Cache> allCaches;


    static {
        //todo 再优化
        executorService = Executors.newCachedThreadPool(new DefaultThreadFactory("changeListener"));
    }

    public AbstractConfig() {
        this.configVersion = new AtomicLong();
        this.allCaches = Lists.newArrayList();
    }

    @Override
    public void addChangeListener(ConfigChangeListener listener) {
        if (!configChangeListeners.contains(listener)) {
            configChangeListeners.add(listener);
        }
    }


    protected void fireConfigChangeEvent(final ConfigChangeEvent event) {
        if (CollectionUtils.isEmpty(configChangeListeners)) {
            return;
        }
        for (final ConfigChangeListener configChangeListener : configChangeListeners) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    configChangeListener.onEvent(event);
                }
            });
        }
    }

    @Override
    public Integer getIntProperty(String key, Integer defaultValue) {

        integerCache = createCaCheIfNecessary(integerCache);
        return getValueFromCache(key, defaultValue, integerCache, TypeTransform.integerFunction);
    }

    @Override
    public Long getLongProperty(String key, Long defaultValue) {
        longCache = createCaCheIfNecessary(longCache);
        return getValueFromCache(key, defaultValue, longCache, TypeTransform.longFunction);
    }

    @Override
    public Short getShortProperty(String key, Short defaultValue) {
        shortCache = createCaCheIfNecessary(shortCache);

        return getValueFromCache(key, defaultValue, shortCache, TypeTransform.shortFunction);
    }

    @Override
    public Byte getByteProperty(String key, Byte defaultValue) {
        byteCache = createCaCheIfNecessary(byteCache);
        return getValueFromCache(key, defaultValue, byteCache, TypeTransform.byteFunction);
    }

    @Override
    public Double getDoubleProperty(String key, Double defaultValue) {
        doubleCache = createCaCheIfNecessary(doubleCache);
        return getValueFromCache(key, defaultValue, doubleCache, TypeTransform.doubleFunction);
    }

    @Override
    public Float getFloatProperty(String key, Float defaultValue) {
        floatCache = createCaCheIfNecessary(floatCache);
        return getValueFromCache(key, defaultValue, floatCache, TypeTransform.flatFunction);
    }

    @Override
    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        booleanCache = createCaCheIfNecessary(booleanCache);
        return getValueFromCache(key, defaultValue, booleanCache, TypeTransform.booleanFunction);
    }

    @Override
    public String[] getArrayProperty(String key, final String delimiter, String[] defaultValue) {
        arrayCache = createCaCheIfNecessary(arrayCache);
        Function<String, String[]> function = new Function<String, String[]>() {
            @Override
            public String[] apply(String input) {
                return input.split(delimiter);
            }
        };
        return getValueFromCache(key, defaultValue, arrayCache, function);
    }

    private <T> Cache<String, T> createCaCheIfNecessary(Cache<String, T> cache) {
        //double check
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    return createCache();
                }
            }
        }
        return cache;
    }

    protected <T> T getValueFromCache(String key, T defaultValue, Cache<String, T> cache, Function<String, T> parser) {
        T result = cache.getIfPresent(key);
        if (result != null) {
            return result;
        }
        return getValueAndSetCache(key, defaultValue, cache, parser);
    }

    protected <T> T getValueAndSetCache(String key, T defaultValue, Cache<String, T> cache, Function<String, T> parser) {
        long currentVersion = configVersion.get();
        String value = getProperty(key, null);
        if (value != null) {
            T result = parser.apply(value);
            if (result != null) {
                synchronized (this) {
                    if (currentVersion == configVersion.get()) {
                        cache.put(key, result);
                    }
                }
                return result;
            }
        }
        return defaultValue;
    }

    private <T> Cache<String, T> createCache() {
        //todo 参数定制化
        Cache<String, T> cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(60, TimeUnit.SECONDS).build();
        allCaches.add(cache);
        return cache;
    }

    protected List<ConfigChange> propertyChanges(String appId, Properties previous, Properties current) {
        List<ConfigChange> changes = Lists.newArrayList();
        if (previous == null) {
            previous = new Properties();
        }
        if (current == null) {
            current = new Properties();
        }

        Set<String> previousKeys = previous.stringPropertyNames();
        Set<String> currentKeys = current.stringPropertyNames();
        Sets.SetView<String> commonKeys = Sets.intersection(previousKeys, currentKeys);
        Sets.SetView<String> newKeys = Sets.difference(currentKeys, commonKeys);
        Sets.SetView<String> removedKeys = Sets.difference(previousKeys, commonKeys);
        if (CollectionUtils.isNotEmpty(newKeys)) {
            for (String newKey : newKeys) {
                changes.add(new ConfigChange(appId, newKey, current.getProperty(newKey), null, ChangeTypeEnum.ADDED));
            }
        }

        if (CollectionUtils.isNotEmpty(removedKeys)) {
            for (String removedKey : removedKeys) {
                changes.add(new ConfigChange(appId, removedKey, null, previous.getProperty(removedKey), ChangeTypeEnum.DELETEED));
            }
        }

        if (CollectionUtils.isNotEmpty(commonKeys)) {
            for (String commonKey : commonKeys) {
                String previousValue = previous.getProperty(commonKey);
                String currentValue = current.getProperty(commonKey);
                if (Objects.equal(previousValue, currentValue)) {
                    continue;
                }
                changes.add(new ConfigChange(appId, commonKey, currentValue, previousValue, ChangeTypeEnum.MODIFIED));
            }
        }
        return changes;
    }
}
