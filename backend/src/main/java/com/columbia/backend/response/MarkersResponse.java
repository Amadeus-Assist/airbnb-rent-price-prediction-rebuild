package com.columbia.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pojo.Marker;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MarkersResponse {
    private Marker[] markers;
}
