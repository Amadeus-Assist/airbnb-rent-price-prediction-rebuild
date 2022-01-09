package com.columbia.backend.service.impl;

import com.columbia.backend.service.GetCityLocationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class GetCityLocationServiceImpl implements GetCityLocationService {
    @Resource(name = "cityAttrMap")
    private Map<String, String[]> cityAttrMap;

    @Override
    public double[] getCenter(String city) {
        if (!cityAttrMap.containsKey(city)){
            return null;
        }
        String[] attrs = cityAttrMap.get(city);
        return new double[]{Double.parseDouble(attrs[0]), Double.parseDouble(attrs[1])};
    }

    @Override
    public int getZoom(String city) {
        if (!cityAttrMap.containsKey(city)){
            return -1;
        }
        String[] attrs = cityAttrMap.get(city);
        return Integer.parseInt(attrs[2]);
    }
}
