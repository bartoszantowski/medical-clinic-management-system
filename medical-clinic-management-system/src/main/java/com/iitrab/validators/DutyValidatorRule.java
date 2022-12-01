package com.iitrab.validators;

import com.iitrab.types.CustomDutyTO;

/**
 * API interface for validating duties
 */
public interface DutyValidatorRule {

    /**
     * Method will call methods in classes that implement the interface.
     *
     * @param customDutyTO {@link CustomDutyTO}
     * @return true or exception
     */
    boolean apply(CustomDutyTO customDutyTO);
}
