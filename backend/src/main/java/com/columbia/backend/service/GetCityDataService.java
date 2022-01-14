package com.columbia.backend.service;

import com.columbia.backend.pojo.DateDataIntPoint;
import com.columbia.backend.pojo.HousingHisPoint;

public interface GetCityDataService {
    DateDataIntPoint[] getCovidHisData(String city, String state, String country, int timeStart, int timeEnd);

    HousingHisPoint[] getHousingHisData(String city, String state, String country, int timeStart, int timeEnd);
}
