package com.iitrab.validators;

import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;

/**
 * API interface for validating visits by doctor
 */
public interface VisitValidatorByDoctorRule {

    /**
     * Method will call methods in classes that implement the interface.
     *
     * @param customVisitDoctorAndSpecializationTO {@link CustomVisitDoctorAndSpecializationTO}
     * @return true or exception
     */
    boolean applyByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO);
}
