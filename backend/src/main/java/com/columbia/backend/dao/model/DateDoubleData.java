package com.columbia.backend.dao.model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class DateDoubleData implements Serializable {
    private static final long serialVersionUID = 8649254790735198574L;

    private int dateInt;
    private double value;
}
