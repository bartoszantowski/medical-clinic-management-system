package com.iitrab.dao.impl;

import com.iitrab.dao.PatientRepoCustom;
import com.iitrab.domain.PatientEntity;
import com.iitrab.exception.api.BusinessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class PatientRepoCustomImpl implements PatientRepoCustom {

    private static final String DATE_FROM = "dateFrom";
    private static final String DATE_TO = "dateTo";
    private static final String NO_PATIENT_WITH_VISIT_STATUS_UNREALIZED = "No patient with visit status: UNREALIZED! ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PatientEntity findPatientWithTheHighestNumberOfVisitWithStatusUnrealized(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new IllegalArgumentException();
        }
        TypedQuery<PatientEntity> query = entityManager.createNamedQuery(PatientEntity.QUERY_FIND_PATIENT_WITH_THE_HIGHEST_NUMBER_OF_VISIT_WITH_STATUS_UNREALIZED, PatientEntity.class);
        query.setParameter(DATE_FROM, dateFrom);
        query.setParameter(DATE_TO, dateTo);

        List<PatientEntity> patients = query.getResultList();

        if (patients.isEmpty()) {
            throw new BusinessException(NO_PATIENT_WITH_VISIT_STATUS_UNREALIZED);
        }
        return patients.get(0);
    }
}
