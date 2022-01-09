package com.columbia.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import pojo.CityAttr;

import javax.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

    @Bean(name = "cityAttrMap")
    public Map<String, CityAttr> getCityAttrMap() {
        Properties cityAttrProps = getProperties(Constants.CITY_ATTRS_PATH);
        Map<String, CityAttr> propsMap = new HashMap<>();
        cityAttrProps.forEach((key, value) -> {
            String city = (String) key;
            String valueStr = (String) value;
            if (isEmpty(valueStr)) {
                logger.error("Invalid City Attrs format!");
                throw new RuntimeException("Invalid City Attrs format!");
            }
            String[] attrs = valueStr.split(",");
            if (attrs.length != 3) {
                logger.error("Invalid City Attrs format!");
                throw new RuntimeException("Invalid City Attrs format!");
            }
            if (!(attrs[0].matches(Constants.NUM_REGEX) && attrs[1].matches(Constants.NUM_REGEX)
                    && attrs[2].matches(Constants.NAT_INT_REGEX))) {
                logger.error("Invalid City Attrs format!");
                throw new RuntimeException("Invalid City Attrs format!");
            }
            double[] location = new double[]{Double.parseDouble(attrs[0]), Double.parseDouble(attrs[1])};
            int zoom = Integer.parseInt(attrs[2]);
            propsMap.put(city, new CityAttr(city, location, zoom));
        });
        return propsMap;
    }

    @Bean(name = "cityArr")
    public String[] getCityArr(){
        Properties cityAttrProps = getProperties(Constants.CITY_ATTRS_PATH);
        String[] cityArr = new String[cityAttrProps.size()];
        int index = 0;
        for(Object city:cityAttrProps.keySet()){
            cityArr[index] = (String) city;
            ++index;
        }
        return cityArr;
    }

    @Bean(name = "cityAliasMap")
    public Map<String, String> getCityAliasMap(){
        Properties cityAliasProps = getProperties(Constants.CITY_ALIAS_PATH);
        Map<String, String> cityAliasMap = new HashMap<>();
        cityAliasProps.forEach((city, aliases) -> {
            String cityStr = (String) city;
            String[] aliasArr = ((String) aliases).split("/");
            for(String alias : aliasArr){
                cityAliasMap.put(alias, cityStr);
            }
        });
        return cityAliasMap;
    }

    @Bean(name = "cityAliasArr")
    public String[] getCityAliasArr(Map<String, String> cityAliasMap){
        logger.info("Length of cityAliasMap in getCityAliasArr(): {}",cityAliasMap.size());
        return cityAliasMap.keySet().toArray(new String[0]);
    }
}
