package com.lewis.configcenter.configclient.factory;

import com.lewis.configcenter.configclient.core.configrepository.ConfigRepository;
import com.lewis.configcenter.configclient.factory.impl.DefaultConfigRepositoryFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultConfigRepositoryFactoryTest {

    @Test
    public void test1() {
        DefaultConfigRepositoryFactory factory = new DefaultConfigRepositoryFactory();
        ConfigRepository localFile = factory.newInstance("snailreader", ConfigRepositoryEnum.LOCAL_FILE);
        ConfigRepository remote = factory.newInstance("snailreader", ConfigRepositoryEnum.REMOTE);
        System.out.println(localFile);
        System.out.println(remote);

    }

    @Test
    public void test2() {

       System.setProperty("baseFileDir","/Users/lewis0077/data/data1");
        DefaultConfigRepositoryFactory factory = new DefaultConfigRepositoryFactory();
        ConfigRepository snailreader = factory.newInstance("snailreader-finance", ConfigRepositoryEnum.LOCAL_FILE);
        System.out.println(snailreader);
    }

}
