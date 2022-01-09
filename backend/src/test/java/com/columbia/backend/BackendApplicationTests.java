package com.columbia.backend;

import com.columbia.backend.service.GetSimilarCityService;
import com.columbia.backend.service.impl.GetSimilarCityServiceImpl;
import com.columbia.backend.utils.LocalBeanFactory;
import com.columbia.backend.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.Resource;

@SpringBootTest
class BackendApplicationTests {
    @Resource
    GetSimilarCityService getSimilarCityService;

    @Test
    void contextLoads() {
//        GetSimilarCityService getSimilarCityService = LocalBeanFactory.getBean(GetSimilarCityServiceImpl.class);
        System.out.println(getSimilarCityService.getSimilarCity("new yorl"));
    }

}
