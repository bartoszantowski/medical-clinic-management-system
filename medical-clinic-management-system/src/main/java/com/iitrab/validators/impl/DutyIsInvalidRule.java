package com.iitrab.validators.impl;

import com.iitrab.exception.api.BusinessException;
import com.iitrab.types.CustomDutyTO;
import com.iitrab.validators.DutyValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Class for basic duty validation.
 * Checks the elements of the compliance of the given duty with applicable regulations.
 */
@Order(100)
@Component
@Slf4j
public class DutyIsInvalidRule implements DutyValidatorRule {

    private static final String OBJECT_MUST_BE_NOT_NULL = "Object must be NOT NULL!";

    @Override
    public boolean apply(CustomDutyTO customDutyTO) {
        check(customDutyTO);
        return true;
    }

    private void check(CustomDutyTO customDutyTO) {
        ifIsNull(customDutyTO);
        ifIsNull(customDutyTO.getDoctorId());
        ifIsNull(customDutyTO.getStartDutyDate());
        ifIsNull(customDutyTO.getEndDutyDate());
    }

    private <T> void ifIsNull(T object) {
        if (object == null) {
            throw new BusinessException(OBJECT_MUST_BE_NOT_NULL);
        }
    }
}
