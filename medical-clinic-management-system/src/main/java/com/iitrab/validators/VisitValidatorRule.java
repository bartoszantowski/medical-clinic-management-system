package com.iitrab.validators;

import com.iitrab.types.CustomVisitSpecializationTO;

/**
 * API interface for validating visits by specialization
 */
public interface VisitValidatorRule {

    /**
     * Method will call methods in classes that implement the interface.
     *
     * @param customVisitSpecializationTO {@link CustomVisitSpecializationTO}
     * @return true or exception
     */
    boolean applyBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO);
}
