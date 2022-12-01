package com.iitrab.service;

import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.types.VisitTO;

/**
 * API interface for performing operations on CustomVisitSpecializationTO
 */
public interface VisitService {

    /**
     * Creates and persists the visit, based on the provided creation data.
     * Multi-level validation is performed before the visit is created.
     *
     * @param customVisitSpecializationTO {@link CustomVisitSpecializationTO}
     * @return added VisitTO
     */
    VisitTO scheduleVisitBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO);


}
