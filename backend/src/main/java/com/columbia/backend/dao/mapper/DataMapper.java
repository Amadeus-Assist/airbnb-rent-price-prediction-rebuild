package com.columbia.backend.dao.mapper;

import com.columbia.backend.dao.model.DateIntData;
import com.columbia.backend.dao.model.HousingHisData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataMapper {

    @Select("SELECT updatetimeint AS dateInt, newcase AS value FROM airbnb.covid WHERE city=#{city} AND state=#{state} AND " +
            "country=#{country} AND updatetimeint BETWEEN ${timeStart} AND ${timeEnd} ORDER BY updatetimeint")
    List<DateIntData> findCovidHistoryData(String city, String state, String country, int timeStart, int timeEnd);

    @Select("SELECT date_int AS date_int, avg_price AS avg_price, median_price AS median_price FROM airbnb" +
            ".housing_count WHERE city=#{city} AND state=#{state} AND country=#{country} AND date_int BETWEEN " +
            "${timeStart} AND ${timeEnd} ORDER BY date_int")
    List<HousingHisData> findHousingHistoryData(String city, String state, String country, int timeStart, int timeEnd);
}
