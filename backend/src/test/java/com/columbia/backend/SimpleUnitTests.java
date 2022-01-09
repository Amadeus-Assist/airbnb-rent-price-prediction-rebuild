package com.columbia.backend;

import org.junit.jupiter.api.Test;

public class SimpleUnitTests {

    @Test
    void testRegex(){
        String regex = "^[+-]?\\d+(\\.\\d+)?$";
        System.out.println("123.98".matches(regex));
    }
}
