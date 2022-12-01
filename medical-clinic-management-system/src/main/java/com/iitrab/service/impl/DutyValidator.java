package com.iitrab.service.impl;

import com.iitrab.types.CustomDutyTO;
import com.iitrab.validators.DutyValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class for validation of duties.
 */
@Component
@Slf4j
class DutyValidator {

    @Autowired
    List<DutyValidatorRule> dutyValidatorRules;

    /**
     * By calling the appropriate validators from the list,
     * the method checks whether the duty can be created.
     *
     * @param customDutyTO {@link CustomDutyTO}
     */
    public void ifDutyCanBeAdded(CustomDutyTO customDutyTO) {
        for (DutyValidatorRule dutyValidatorRule : dutyValidatorRules) {
            dutyValidatorRule.apply(customDutyTO);
        }
    }
}
