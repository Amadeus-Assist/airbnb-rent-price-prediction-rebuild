package com.columbia.backend.dao.model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class HousingHisData implements Serializable {

    private static final long serialVersionUID = 7847798879627013204L;

    private int date_int;
    private double avg_price;
    private double median_price;
}
