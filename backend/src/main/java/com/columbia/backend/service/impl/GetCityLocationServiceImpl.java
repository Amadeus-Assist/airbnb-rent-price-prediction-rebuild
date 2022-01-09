package com.columbia.backend.service.impl;

import com.columbia.backend.service.GetCityLocationService;
import org.springframework.stereotype.Service;
import pojo.CityAttr;
import pojo.Marker;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Service
public class GetCityLocationServiceImpl implements GetCityLocationService {
    @Resource(name = "cityAttrMap")
    private Map<String, CityAttr> cityAttrMap;

    @Override
    public CityAttr getCityAttr(String city) {
        if (!cityAttrMap.containsKey(city)){
            return null;
        }
        return cityAttrMap.get(city);
    }

    @Override
    public double[] getCenter(String city) {
        return Optional.ofNullable(getCityAttr(city)).map(CityAttr::getLocation).orElse(null);
    }

    @Override
    public int getZoom(String city) {
        return Optional.ofNullable(getCityAttr(city)).map(CityAttr::getZoom).orElse(-1);
    }

    @Override
    public Marker[] getAllMarkers() {
        Marker[] markers = new Marker[cityAttrMap.size()];
        int index = 0;
        for(String city : cityAttrMap.keySet()){
            CityAttr attr = cityAttrMap.get(city);
            markers[index++] = new Marker(attr.getName(), attr.getLocation());
        }
        return markers;
    }
}
