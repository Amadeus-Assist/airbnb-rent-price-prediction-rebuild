package com.columbia.backend.service.impl;

import com.columbia.backend.dao.mapper.DataMapper;
import com.columbia.backend.dao.model.DateIntData;
import com.columbia.backend.pojo.DateDataIntPoint;
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
    public DateDataIntPoint[] getHistoryData(String city, String state, String country, int timeStart, int timeEnd) {
        List<DateIntData> dataList = mapper.findCovidHistoryData(city, state, country, timeStart, timeEnd);
        int size = dataList.size();
        DateDataIntPoint[] historyData = new DateDataIntPoint[size];
        int idx = 0;
        for (DateIntData oneDay : dataList) {
            DateTime dt = new DateTime(oneDay.getDateInt() * 1000L);
            historyData[idx] = new DateDataIntPoint(dt.toString(Constants.DATE_SHOWN_PATTERN),
                    oneDay.getValue());
            idx++;
        }
        return historyData;
    }
}
