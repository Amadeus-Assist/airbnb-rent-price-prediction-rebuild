package com.columbia.backend.controller;

import com.columbia.backend.annos.CityCheck;
import com.columbia.backend.pojo.CityAttr;
import com.columbia.backend.pojo.IntDataContainer;
import com.columbia.backend.response.CityLocResponse;
import com.columbia.backend.response.CityNameResponse;
import com.columbia.backend.response.CovidHistoryDataResponse;
import com.columbia.backend.response.MarkersResponse;
import com.columbia.backend.service.GetCityDataService;
import com.columbia.backend.service.GetCityLocationService;
import com.columbia.backend.service.GetSimilarCityService;
import com.columbia.backend.utils.Constants;
import com.sun.istack.NotNull;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

@RestController
@Validated
public class CityDataController {
    private static final Logger logger = LoggerFactory.getLogger(CityDataController.class);

    @Resource
    private Properties settingProps;
    @Resource
    private Map<String, CityAttr> cityAttrMap;
    @Resource
    private GetSimilarCityService getSimilarCityService;
    @Resource
    private GetCityLocationService getCityLocationService;
    @Resource
    private GetCityDataService getCityDataService;

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
    public MarkersResponse getMarkers() {
        logger.info("Markers: {}", Arrays.toString(getCityLocationService.getAllMarkers()));
        return new MarkersResponse(getCityLocationService.getAllMarkers());
    }

    @ResponseBody
    @GetMapping("/history/covid/{city}")
    public CovidHistoryDataResponse getCovidHistoryData(@NotNull @PathVariable("city") @CityCheck String city) {
        String displayDaysStr = settingProps.getProperty(Constants.HIS_DISPLAY_DAYS_KEY);
        if (!displayDaysStr.matches(Constants.NAT_INT_REGEX)) {
            logger.error("Invalid {}: {}", Constants.HIS_DISPLAY_DAYS_KEY, displayDaysStr);
            throw new RuntimeException("Invalid history_display_days");
        }
        int displayDays = Integer.parseInt(displayDaysStr);
        DateTime end = DateTime.now();
        DateTime start = end.minusDays(displayDays);
        int endTime = (int) (end.getMillis() / 1000);
        int startTime = (int) (start.getMillis() / 1000);
        CityAttr attr = cityAttrMap.get(city);
        IntDataContainer dataContainer = getCityDataService.getHistoryData(attr.getDbCity(), attr.getDbState(),
                attr.getDbCountry(), startTime, endTime);
        logger.info("Returned history data length: {}", dataContainer.getData().length);
        return new CovidHistoryDataResponse(dataContainer.getDate(), dataContainer.getData());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
