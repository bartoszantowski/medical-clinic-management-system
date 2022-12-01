package com.iitrab.validators.impl;

import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.validators.VisitValidatorByDoctorRule;
import com.iitrab.validators.VisitValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Class for patient validation.
 * Checks of the compliance of the given patient with applicable regulations.
 */
@Order(200)
@Component
@Slf4j
public class VisitDateIsInvalidRule implements VisitValidatorRule, VisitValidatorByDoctorRule {

    @Autowired
    private DateTimeValidator dateTimeValidator;


    @Override
    public boolean applyBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        dateTimeValidator.validVisitDateTime(customVisitSpecializationTO.getVisitDate());
        return true;
    }

    @Override
    public boolean applyByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        dateTimeValidator.validVisitDateTime(customVisitDoctorAndSpecializationTO.getVisitDate());
        return true;
    }
}
