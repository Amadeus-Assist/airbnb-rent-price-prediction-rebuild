package com.columbia.backend.service;

import com.columbia.backend.pojo.DateDataIntPoint;

public interface GetCityDataService {
    DateDataIntPoint[] getHistoryData(String city, String state, String country, int timeStart, int timeEnd);
}
