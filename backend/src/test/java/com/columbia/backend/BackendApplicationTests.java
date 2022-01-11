package com.columbia.backend;

import com.columbia.backend.pojo.IntDataContainer;
import com.columbia.backend.service.GetCityDataService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BackendApplicationTests {
    @Resource
    private GetCityDataService getCityDataService;

    @Test
    void contextLoads() {
        DateTime timeStart = DateTime.now();
        IntDataContainer data = getCityDataService.getHistoryData("New York", "New York", "US", 1617166800, 1641708000);
        System.out.format("Query spends %d millis.", new Duration(timeStart,DateTime.now()).getMillis());
        System.out.println(data);
//        GetSimilarCityService getSimilarCityService = LocalBeanFactory.getBean(GetSimilarCityServiceImpl.class);
//        System.out.println(getSimilarCityService.getSimilarCity("new yorl"));
    }

}
