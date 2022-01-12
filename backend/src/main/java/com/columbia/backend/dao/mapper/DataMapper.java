package com.columbia.backend.dao.mapper;

import com.columbia.backend.dao.model.DateIntData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataMapper {

    @Select("SELECT updatetimeint AS dateInt, newcase AS value FROM airbnb.covid WHERE city=#{city} AND state=#{state} AND " +
            "country=#{country} AND updatetimeint BETWEEN ${timeStart} AND ${timeEnd} ORDER BY updatetimeint")
    List<DateIntData> findCovidHistoryData(String city, String state, String country, int timeStart, int timeEnd);
}
