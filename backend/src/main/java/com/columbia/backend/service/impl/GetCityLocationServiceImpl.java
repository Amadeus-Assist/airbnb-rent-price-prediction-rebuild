package com.columbia.backend.service.impl;

import com.columbia.backend.service.GetCityLocationService;
import org.springframework.stereotype.Service;

@Service
public class GetCityLocationServiceImpl implements GetCityLocationService {

    @Override
    public double[] getCenter(String city) {
        return new double[]{40.71, -74};
    }

    @Override
    public int getZoom(String city) {
        return 11;
    }
}
