package com.columbia.backend.controller;

import com.columbia.backend.annos.CityCheck;
import com.columbia.backend.pojo.CityAttr;
import com.columbia.backend.pojo.DateDoublePoint;
import com.columbia.backend.pojo.DateIntPoint;
import com.columbia.backend.pojo.HousingHisPoint;
import com.columbia.backend.response.*;
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
        String displayDaysStr = settingProps.getProperty(Constants.COVID_HIS_DISPLAY_DAYS_KEY);
        if (!displayDaysStr.matches(Constants.NAT_INT_REGEX)) {
            logger.error("Invalid {}: {}", Constants.COVID_HIS_DISPLAY_DAYS_KEY, displayDaysStr);
            throw new RuntimeException("Invalid covid_history_display_days");
        }
        int displayDays = Integer.parseInt(displayDaysStr);
        int[] timeArr = getStartEndDataIntBefore(displayDays);
        int startTime = timeArr[0];
        int endTime = timeArr[1];
        CityAttr attr = cityAttrMap.get(city);
        DateIntPoint[] historyData = getCityDataService.getCovidHisData(attr.getDbCity(), attr.getDbState(),
                attr.getDbCountry(), startTime, endTime);
        return new CovidHistoryDataResponse(historyData);
    }

    @ResponseBody
    @GetMapping("/history/housing/{city}")
    public HousingHistoryDataResponse getHousingHistoryData(@NotNull @PathVariable("city") @CityCheck String city) {
        String displayDaysStr = settingProps.getProperty(Constants.HOUSING_HIS_DISPLAY_DAYS_KEY);
        if (!displayDaysStr.matches(Constants.NAT_INT_REGEX)) {
            logger.error("Invalid {}: {}", Constants.HOUSING_HIS_DISPLAY_DAYS_KEY, displayDaysStr);
            throw new RuntimeException("Invalid housing_history_display_days");
        }
        int displayDays = Integer.parseInt(displayDaysStr);
        int[] timeArr = getStartEndDataIntBefore(displayDays);
        int startTime = timeArr[0];
        int endTime = timeArr[1];
        CityAttr attr = cityAttrMap.get(city);
        HousingHisPoint[] historyData = getCityDataService.getHousingHisData(attr.getDbCity(), attr.getDbState(),
                attr.getDbCountry(), startTime, endTime);
        return new HousingHistoryDataResponse(historyData);
    }

    @ResponseBody
    @GetMapping("/prediction/covid/{city}")
    public CovidPredictionDataResponse getCovidPredictionData(@NotNull @PathVariable("city") @CityCheck String city){
        String displayDaysStr = settingProps.getProperty(Constants.COVID_PRE_DISPLAY_DAYS_KEY);
        if (!displayDaysStr.matches(Constants.NAT_INT_REGEX)) {
            logger.error("Invalid {}: {}", Constants.COVID_PRE_DISPLAY_DAYS_KEY, displayDaysStr);
            throw new RuntimeException("Invalid covid_prediction_display_days");
        }
        int displayDays = Integer.parseInt(displayDaysStr);
        int[] timeArr = getStartEndDataIntAfter(displayDays);
        int startTime = timeArr[0];
        int endTime = timeArr[1];
        CityAttr attr = cityAttrMap.get(city);
        DateIntPoint[] predictionData = getCityDataService.getCovidPreData(attr.getDbCity(), attr.getDbState(),
                attr.getDbCountry(), startTime, endTime);
        return new CovidPredictionDataResponse(predictionData);
    }

    @ResponseBody
    @GetMapping("/prediction/housing/{city}")
    public HousingPredictionDataResponse getHousingPredictionData(@NotNull @PathVariable("city") @CityCheck String city){
        String displayDaysStr = settingProps.getProperty(Constants.HOUSING_PRE_DISPLAY_DAYS_KEY);
        if (!displayDaysStr.matches(Constants.NAT_INT_REGEX)) {
            logger.error("Invalid {}: {}", Constants.HOUSING_PRE_DISPLAY_DAYS_KEY, displayDaysStr);
            throw new RuntimeException("Invalid housing_prediction_display_days");
        }
        int displayDays = Integer.parseInt(displayDaysStr);
        int[] timeArr = getStartEndDataIntAfter(displayDays);
        int startTime = timeArr[0];
        int endTime = timeArr[1];
        CityAttr attr = cityAttrMap.get(city);
        DateDoublePoint[] predictionData = getCityDataService.getHousingPreData(attr.getDbCity(), attr.getDbState(),
                attr.getDbCountry(), startTime, endTime);
        return new HousingPredictionDataResponse(predictionData);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }



    public int[] getStartEndDataIntBefore(int days){
        DateTime end = DateTime.now().withTimeAtStartOfDay().minusDays(1);
        DateTime start = end.minusDays(days-1);
        int endTime = (int) (end.getMillis() / 1000);
        int startTime = (int) (start.getMillis() / 1000);
        return new int[]{startTime, endTime};
    }

    public int[] getStartEndDataIntAfter(int days){
        DateTime start = DateTime.now().withTimeAtStartOfDay();
        DateTime end = start.plusDays(days-1);
        int endTime = (int) (end.getMillis() / 1000);
        int startTime = (int) (start.getMillis() / 1000);
        return new int[]{startTime, endTime};
    }
}
