package com.columbia.backend.service;

import com.columbia.backend.pojo.DateDoublePoint;
import com.columbia.backend.pojo.DateIntPoint;
import com.columbia.backend.pojo.HousingHisPoint;

public interface GetCityDataService {
    DateIntPoint[] getCovidHisData(String city, String state, String country, int timeStart, int timeEnd);

    HousingHisPoint[] getHousingHisData(String city, String state, String country, int timeStart, int timeEnd);

    DateIntPoint[] getCovidPreData(String city, String state, String country, int timeStart, int timeEnd);

    DateDoublePoint[] getHousingPreData(String city, String state, String country, int timeStart, int timeEnd);
}
