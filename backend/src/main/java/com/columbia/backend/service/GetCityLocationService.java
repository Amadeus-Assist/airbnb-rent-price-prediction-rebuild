package com.columbia.backend.service;

import com.columbia.backend.response.CityLocResponse;

public interface GetCityLocationService {
    double[] getCenter(String city);
    int getZoom(String city);
}
