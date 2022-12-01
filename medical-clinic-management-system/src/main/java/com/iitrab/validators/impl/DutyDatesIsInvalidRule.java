package com.iitrab.validators.impl;

import com.iitrab.types.CustomDutyTO;
import com.iitrab.validators.DutyValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(200)
@Component
@Slf4j
public class DutyDatesIsInvalidRule implements DutyValidatorRule {

    @Autowired
    private DateTimeValidator dateTimeValidator;

    @Override
    public boolean apply(CustomDutyTO customDutyTO) {
        dateTimeValidator.validDutyDateTime(customDutyTO.getStartDutyDate(), customDutyTO.getEndDutyDate());
        return true;
    }
}
