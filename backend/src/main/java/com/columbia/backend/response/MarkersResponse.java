package com.columbia.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.columbia.backend.pojo.Marker;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MarkersResponse {
    private Marker[] markers;
}
