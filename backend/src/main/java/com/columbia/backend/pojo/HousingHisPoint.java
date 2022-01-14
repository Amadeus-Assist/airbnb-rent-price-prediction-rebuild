package com.columbia.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HousingHisPoint {
    private String label;
    private double avg_price;
    private double median_price;
}
