package com.columbia.backend.service.impl;

import com.columbia.backend.service.GetSimilarCityService;
import com.columbia.backend.utils.EditDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class GetSimilarCityServiceImpl implements GetSimilarCityService {
    private static final Logger logger = LoggerFactory.getLogger(GetSimilarCityServiceImpl.class);

    @Resource(name = "cityAliasArr")
    private String[] cityAliasArr;
    @Resource(name = "cityAliasMap")
    private Map<String, String> cityAliasMap;
    @Resource
    private EditDistance ed;

    @Override
    public String getSimilarCity(String cityInput) {
        if (cityAliasMap.containsKey(cityInput)) {
            return cityAliasMap.get(cityInput);
        }
        double minScore = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < cityAliasArr.length; i++) {
            double localScore = ed.score(cityAliasArr[i], cityInput);
            if (localScore < minScore) {
                minScore = localScore;
                minIndex = i;
            }
        }
        return cityAliasMap.get(cityAliasArr[minIndex]);
    }
}
