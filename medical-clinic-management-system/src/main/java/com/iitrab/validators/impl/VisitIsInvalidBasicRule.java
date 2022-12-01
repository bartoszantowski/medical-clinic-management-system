package com.iitrab.validators.impl;

import com.iitrab.exception.api.BusinessException;
import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.validators.VisitValidatorByDoctorRule;
import com.iitrab.validators.VisitValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(100)
@Component
@Slf4j
public class VisitIsInvalidBasicRule implements VisitValidatorRule, VisitValidatorByDoctorRule {

    private static final String OBJECT_MUST_BE_NOT_NULL = "Object must be NOT NULL!";

    @Override
    public boolean applyBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        checkBySpecialization(customVisitSpecializationTO);
        return true;
    }

    @Override
    public boolean applyByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        checkByDoctor(customVisitDoctorAndSpecializationTO);
        return true;
    }

    private void checkBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        ifIsNull(customVisitSpecializationTO);
        ifIsNull(customVisitSpecializationTO.getPatientId());
        ifIsNull(customVisitSpecializationTO.getDoctorSpecializationType());
        ifIsNull(customVisitSpecializationTO.getVisitDate());
    }

    private void checkByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        ifIsNull(customVisitDoctorAndSpecializationTO);
        ifIsNull(customVisitDoctorAndSpecializationTO.getPatientId());
        ifIsNull(customVisitDoctorAndSpecializationTO.getDoctorId());
        ifIsNull(customVisitDoctorAndSpecializationTO.getDoctorSpecializationType());
        ifIsNull(customVisitDoctorAndSpecializationTO.getVisitDate());
    }

    private <T> void ifIsNull(T object) {
        if (object == null) {
            throw new BusinessException(OBJECT_MUST_BE_NOT_NULL);
        }
    }
}
