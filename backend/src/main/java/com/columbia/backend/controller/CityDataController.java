package com.columbia.backend.controller;

import com.columbia.backend.response.CityNameResponse;
import com.columbia.backend.service.GetSimilarCityService;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class CityDataController {
    private static final Logger logger = LoggerFactory.getLogger(CityDataController.class);

    @Resource
    private GetSimilarCityService getSimilarCityService;

    @ResponseBody
    @GetMapping("/getSimilarCity/{cityInput}")
    public CityNameResponse getSimilarCity(@NotNull @PathVariable("cityInput") String cityInput) {
        logger.info("cityInput: {}", cityInput);
        String city = getSimilarCityService.getSimilarCity(cityInput);
        logger.info("returned city: {}", city);
        return new CityNameResponse(city);
    }
}
