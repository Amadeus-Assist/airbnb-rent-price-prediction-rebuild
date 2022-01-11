package com.columbia.backend.annos;

import com.columbia.backend.pojo.CityAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class CityCheckValidator implements ConstraintValidator<CityCheck, String> {
    private final static Logger logger = LoggerFactory.getLogger(CityCheckValidator.class);

    @Resource(name = "cityAttrMap")
    private Map<String, CityAttr> cityAttrMap;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return cityAttrMap.containsKey(value);
    }
}
