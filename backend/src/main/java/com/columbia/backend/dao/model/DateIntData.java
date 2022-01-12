package com.columbia.backend.dao.model;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class DateIntData implements Serializable {

    private static final long serialVersionUID = 6892352566106711630L;

    private int dateInt;
    private int value;
}
