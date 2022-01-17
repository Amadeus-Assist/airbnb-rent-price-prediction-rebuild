package com.columbia.backend.service.impl;

import com.columbia.backend.dao.mapper.DataMapper;
import com.columbia.backend.dao.model.DateDoubleData;
import com.columbia.backend.dao.model.DateIntData;
import com.columbia.backend.dao.model.HousingHisData;
import com.columbia.backend.pojo.DateDoublePoint;
import com.columbia.backend.pojo.DateIntPoint;
import com.columbia.backend.pojo.HousingHisPoint;
import com.columbia.backend.service.GetCityDataService;
import com.columbia.backend.utils.Constants;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetCityDataServiceImpl implements GetCityDataService {
    @Resource
    private DataMapper mapper;

    @Override
    public DateIntPoint[] getCovidHisData(String city, String state, String country, int timeStart, int timeEnd) {
        List<DateIntData> dataList = mapper.findCovidHistoryData(city, state, country, timeStart, timeEnd);
        return capDateIntArray(dataList);
    }

    @Override
    public HousingHisPoint[] getHousingHisData(String city, String state, String country, int timeStart, int timeEnd) {
        List<HousingHisData> rawDataList = mapper.findHousingHistoryData(city, state, country, timeStart, timeEnd);
        int size = rawDataList.size();
        HousingHisPoint[] historyData = new HousingHisPoint[size];
        int idx = 0;
        for (HousingHisData data : rawDataList) {
            DateTime dt = new DateTime(data.getDate_int() * 1000L);
            historyData[idx] = new HousingHisPoint(dt.toString(Constants.DATE_YMD_PATTERN),
                    data.getAvg_price(), data.getMedian_price());
            idx++;
        }
        return historyData;
    }

    @Override
    public DateIntPoint[] getCovidPreData(String city, String state, String country, int timeStart, int timeEnd) {
        List<DateIntData> dataList = mapper.findCovidPredictionData(city, state, country, timeStart, timeEnd);
        return capDateIntArray(dataList);
    }

    @Override
    public DateDoublePoint[] getHousingPreData(String city, String state, String country, int timeStart, int timeEnd) {
        List<DateDoubleData> dataList = mapper.findHousingPredictedData(city, state, country, timeStart, timeEnd);
        return capDateDoubleArray(dataList);
    }


    private DateIntPoint[] capDateIntArray(List<DateIntData> dataList) {
        int size = dataList.size();
        DateIntPoint[] dataArr = new DateIntPoint[size];
        int idx = 0;
        for (DateIntData oneDay : dataList) {
            DateTime dt = new DateTime(oneDay.getDateInt() * 1000L);
            dataArr[idx] = new DateIntPoint(dt.toString(Constants.DATE_MD_PATTERN),
                    oneDay.getValue());
            idx++;
        }
        return dataArr;
    }

    private DateDoublePoint[] capDateDoubleArray(List<DateDoubleData> dataList) {
        int size = dataList.size();
        DateDoublePoint[] dataArr = new DateDoublePoint[size];
        int idx = 0;
        for (DateDoubleData oneDay : dataList) {
            DateTime dt = new DateTime(oneDay.getDateInt() * 1000L);
            dataArr[idx] = new DateDoublePoint(dt.toString(Constants.DATE_MD_PATTERN),
                    oneDay.getValue());
            idx++;
        }
        return dataArr;
    }
}
