package com.columbia.backend.service;

import com.columbia.backend.pojo.IntDataContainer;

public interface GetCityDataService {
    IntDataContainer getHistoryData(String city, String state, String country, int timeStart, int timeEnd);
}
