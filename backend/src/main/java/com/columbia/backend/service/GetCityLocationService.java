package com.columbia.backend.service;

import com.columbia.backend.pojo.CityAttr;
import com.columbia.backend.pojo.Marker;

public interface GetCityLocationService {
    CityAttr getCityAttr(String city);
    double[] getCenter(String city);
    int getZoom(String city);
    Marker[] getAllMarkers();
}
