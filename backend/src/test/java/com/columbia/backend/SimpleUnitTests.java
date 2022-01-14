package com.columbia.backend;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

public class SimpleUnitTests {

    @Test
    void testRegex(){
        DateTime end = DateTime.now();
        DateTime start = end.minusDays(1460);
        int endTime = (int) (end.getMillis() / 1000);
        int startTime = (int) (start.getMillis() / 1000);
        System.out.printf("endtime: %d\n", endTime);
        System.out.printf("starttime: %d\n", startTime);
    }
}
