package com.columbia.backend.utils;

import com.alibaba.fastjson.JSON;
import com.columbia.backend.pojo.CityAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.columbia.backend.utils.Utils.isEmpty;

@Component
public class BeanProcessor {
    private final static Logger logger = LoggerFactory.getLogger(BeanProcessor.class);

    @Resource
    private PropertiesReader propsReader;

    @Bean(name = "settingProps")
    public Properties getSettingProps(){
        return propsReader.getProperties(Constants.SETTINGS_PATH);
    }

    @Bean(name = "cityAttrMap")
    public Map<String, CityAttr> getCityAttrMap() {
        Properties cityAttrProps = propsReader.getProperties(Constants.CITY_ATTRS_PATH);
        Map<String, CityAttr> propsMap = new HashMap<>();
        cityAttrProps.forEach((key, value) -> {
            String city = (String) key;
            String valueStr = (String) value;
            if (isEmpty(valueStr)) {
                logger.error("Invalid City Attrs format!");
                throw new RuntimeException("Invalid City Attrs format!");
            }
            CityAttr attr = JSON.parseObject(valueStr, CityAttr.class);
            propsMap.put(city, attr);
        });
        return propsMap;
    }

    @Bean(name = "cityArr")
    public String[] getCityArr(){
        Properties cityAttrProps = propsReader.getProperties(Constants.CITY_ATTRS_PATH);
        String[] cityArr = new String[cityAttrProps.size()];
        int index = 0;
        for(Object city:cityAttrProps.keySet()){
            cityArr[index] = (String) city;
            ++index;
        }
        return cityArr;
    }

    @Bean(name = "cityAliasMap")
    public Map<String, String> getCityAliasMap(Map<String, CityAttr> cityAttrMap){
        Map<String, String> cityAliasMap = new HashMap<>();
        cityAttrMap.forEach((city, attr) -> {
            for(String alias : attr.getAlias()){
                cityAliasMap.put(alias, city);
            }
        });
        return cityAliasMap;
    }

    @Bean(name = "cityAliasArr")
    public String[] getCityAliasArr(Map<String, String> cityAliasMap){
        return cityAliasMap.keySet().toArray(new String[0]);
    }
}
