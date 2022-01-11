package com.columbia.backend.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.columbia.backend.pojo.CityAttr;

import javax.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.columbia.backend.utils.Utils.isEmpty;

@Component
public class PropertiesReader {
    private final static Logger logger = LoggerFactory.getLogger(PropertiesReader.class);

    private final Map<String, Properties> propertiesMap;
    @Resource
    private ResourceLoader resourceLoader;

    PropertiesReader() {
        this.propertiesMap = new HashMap<>();
    }

    public Properties getProperties(String path) {
        if (isEmpty(path)) {
            logger.error("getProperties input with empty string!");
            throw new RuntimeException("getProperties input with empty string!");
        }
        if (propertiesMap.containsKey(path)) {
            return propertiesMap.get(path);
        }
        Properties props = new Properties();
        try {
            props.load(new FileReader(resourceLoader.getResource(path).getFile()));
        } catch (IOException e) {
            logger.error("Can't load properties! {}", e.toString());
            throw new RuntimeException("Can't load properties! {}", e);
        }
        propertiesMap.put(path, props);
        return props;
    }
}
