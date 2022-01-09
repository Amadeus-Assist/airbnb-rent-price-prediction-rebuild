package com.columbia.backend.service;

public interface GetCityLocationService {
    double[] getCenter(String city);
    int getZoom(String city);
}
