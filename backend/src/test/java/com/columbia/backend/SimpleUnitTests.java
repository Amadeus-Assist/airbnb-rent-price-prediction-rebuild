package com.columbia.backend;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

public class SimpleUnitTests {

    @Test
    void testRegex(){
        DateTime date = DateTime.now().withTimeAtStartOfDay();
        System.out.println(date);
    }
}
