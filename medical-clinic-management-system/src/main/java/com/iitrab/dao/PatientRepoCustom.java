package com.iitrab.dao;

import com.iitrab.domain.PatientEntity;

import java.time.LocalDateTime;

public interface PatientRepoCustom {

    /**
     * Query finds the patient who has the most number of visits with the status unrealized
     * (UNREALIZED_PATIENT or UNREALIZED_OTHERS)
     *
     * @param dateFrom date from
     * @param dateTo date to
     * @return matching patient or exception
     */
    PatientEntity findPatientWithTheHighestNumberOfVisitWithStatusUnrealized(LocalDateTime dateFrom, LocalDateTime dateTo);
}
