package com.columbia.backend.response;

import com.columbia.backend.pojo.DateIntPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CovidPredictionDataResponse {
    private DateIntPoint[] covidPrediction;
}
