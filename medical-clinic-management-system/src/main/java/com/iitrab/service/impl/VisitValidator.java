package com.iitrab.service.impl;

import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.validators.VisitValidatorByDoctorRule;
import com.iitrab.validators.VisitValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class for validation of duties.
 */
@Component
@Slf4j
public class VisitValidator {

    @Autowired
    List<VisitValidatorRule> visitValidatorRules;

    @Autowired
    List<VisitValidatorByDoctorRule> visitValidatorByDoctorRules;

    /**
     * By calling the appropriate validators from the list,
     * the method checks whether the visit can be created.
     *
     * @param customVisitSpecializationTO {@link CustomVisitSpecializationTO}
     */
    public void ifVisitBySpecializationCanBeAdded(CustomVisitSpecializationTO customVisitSpecializationTO) {
        for (VisitValidatorRule visitValidatorRules : visitValidatorRules) {
            visitValidatorRules.applyBySpecialization(customVisitSpecializationTO);
        }
    }

    /**
     * By calling the appropriate validators from the list,
     * the method checks whether the visit can be created.
     *
     * @param customVisitDoctorAndSpecializationTO {@link CustomVisitDoctorAndSpecializationTO}
     */
    public void ifVisitByDoctorCanBeAdded(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        for (VisitValidatorByDoctorRule visitValidatorByDoctorRules : visitValidatorByDoctorRules) {
            visitValidatorByDoctorRules.applyByDoctor(customVisitDoctorAndSpecializationTO);
        }
    }
}
