package com.columbia.backend.controller;

import com.columbia.backend.annos.CityCheck;
import com.columbia.backend.response.CityLocResponse;
import com.columbia.backend.response.CityNameResponse;
import com.columbia.backend.response.MarkersResponse;
import com.columbia.backend.service.GetCityLocationService;
import com.columbia.backend.service.GetSimilarCityService;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@RestController
@Validated
public class CityDataController {
    private static final Logger logger = LoggerFactory.getLogger(CityDataController.class);

    @Resource
    private GetSimilarCityService getSimilarCityService;
    @Resource
    private GetCityLocationService getCityLocationService;

    @ResponseBody
    @GetMapping("/getSimilarCity/{cityInput}")
    public CityNameResponse getSimilarCity(@NotNull @PathVariable("cityInput") String cityInput) {
        logger.info("cityInput: {}", cityInput);
        String city = getSimilarCityService.getSimilarCity(cityInput);
        logger.info("returned city: {}", city);
        return new CityNameResponse(city);
    }

    @ResponseBody
    @GetMapping("/cityview/{city}")
    public CityLocResponse getCityLocation(@NotNull @PathVariable("city") @CityCheck String city) {
        double[] center = getCityLocationService.getCenter(city);
        int zoom = getCityLocationService.getZoom(city);
        return new CityLocResponse(center, zoom);
    }

    @ResponseBody
    @GetMapping("/markers")
    public MarkersResponse getMarkers(){
        logger.info("Markers: {}", Arrays.toString(getCityLocationService.getAllMarkers()));
        return new MarkersResponse(getCityLocationService.getAllMarkers());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
