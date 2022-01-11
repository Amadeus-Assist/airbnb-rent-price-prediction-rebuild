package com.columbia.backend.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CityAttr {
    @JSONField(name = "CITY")
    private String city;
    @JSONField(name = "LOCATION")
    private double[] location;
    @JSONField(name = "ZOOM")
    private int zoom;
    @JSONField(name = "ALIAS")
    private String[] alias;
    @JSONField(name = "DB_CITY")
    private String dbCity;
    @JSONField(name = "DB_STATE")
    private String dbState;
    @JSONField(name = "DB_COUNTRY")
    private String dbCountry;
}
