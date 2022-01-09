package com.columbia.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static Character[] asCharacterArray(String s) {
        Character[] split = new Character[s.length()];
        for (int i = 0; i < split.length; i++) {
            split[i] = s.charAt(i);
        }
        return split;
    }
}
