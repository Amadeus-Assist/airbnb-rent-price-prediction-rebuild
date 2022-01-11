package com.columbia.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CovidHistoryDataResponse {
    private String[] covidHistoryDate;
    private int[] covidHistoryData;
}
