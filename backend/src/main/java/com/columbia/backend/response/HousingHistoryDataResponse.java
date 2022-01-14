package com.columbia.backend.response;

import com.columbia.backend.pojo.HousingHisPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HousingHistoryDataResponse {
    private HousingHisPoint[] housingHistory;
}
