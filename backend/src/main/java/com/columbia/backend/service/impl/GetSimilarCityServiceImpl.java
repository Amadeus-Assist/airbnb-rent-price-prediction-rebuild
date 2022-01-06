package com.columbia.backend.service.impl;

import com.columbia.backend.service.GetSimilarCityService;
import org.springframework.stereotype.Service;

@Service
public class GetSimilarCityServiceImpl implements GetSimilarCityService {
    @Override
    public String getSimilarCity(String cityInput) {
        return "nyc";
    }
}
