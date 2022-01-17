package com.columbia.backend.response;

import com.columbia.backend.pojo.DateDoublePoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HousingPredictionDataResponse {
    private DateDoublePoint[] housingPrediction;
}
