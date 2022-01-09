package com.columbia.backend.service;

import pojo.CityAttr;
import pojo.Marker;

public interface GetCityLocationService {
    CityAttr getCityAttr(String city);
    double[] getCenter(String city);
    int getZoom(String city);
    Marker[] getAllMarkers();
}
