package com.iitrab.service;

import com.iitrab.types.CustomDutyTO;
import com.iitrab.types.DutyTO;

/**
 * API interface for performing operations on CustomDutyTO
 */
public interface DutyService {

    /**
     * Creates and persists the duty, based on the provided creation data.
     * Multi-level validation is performed before the duty is created.
     *
     * @param customDutyTO {@link CustomDutyTO}
     * @return added DutyTO
     */
    DutyTO addDoctorsDuty(CustomDutyTO customDutyTO);

}
