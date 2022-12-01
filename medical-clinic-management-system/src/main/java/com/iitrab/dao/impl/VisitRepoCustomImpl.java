package com.iitrab.dao.impl;

import com.iitrab.dao.VisitRepoCustom;
import com.iitrab.domain.*;
import com.iitrab.exception.api.NotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisitRepoCustomImpl implements VisitRepoCustom {

    private static final int MONTHS_TO_SUBTRACT = 1;
    private static final String VISIT_STATUS = "visitStatus";
    private static final String VISIT_DATE = "visitDate";
    private static final String SPECIALIZATION = "specialization";
    private static final String PATIENT = "patient";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String NO_SPECIALIZATION_TYPE_WITH_STATUS_UNREALIZED = "No specialization type with status: UNREALIZED!";
    private static final String PATIENT_ID = "patientId";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VisitEntity> findAllVisitsByCriteria(VisitSearchCriteria visitSearchCriteria) {
        if (visitSearchCriteria == null) {
            return List.of();
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VisitEntity> criteriaQuery = builder.createQuery(VisitEntity.class);
        Root<VisitEntity> visitEntityRoot = criteriaQuery.from(VisitEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (visitSearchCriteria.getVisitStatus() != null) {
            Predicate visitStatusPredicate = builder.equal(visitEntityRoot.get(VISIT_STATUS), visitSearchCriteria.getVisitStatus());
            predicates.add(visitStatusPredicate);
        }
        if (visitSearchCriteria.getDateFrom() != null) {
            Predicate dateFromPredicate = builder.greaterThanOrEqualTo(visitEntityRoot.get(VISIT_DATE), visitSearchCriteria.getDateFrom());
            predicates.add(dateFromPredicate);
        }
        if (visitSearchCriteria.getDateTo() != null) {
            Predicate dateToPredicate = builder.lessThanOrEqualTo(visitEntityRoot.get(VISIT_DATE), visitSearchCriteria.getDateTo());
            predicates.add(dateToPredicate);
        }
        if (visitSearchCriteria.getSpecializationType() != null) {
            Predicate specializationTypePredicate = builder.equal(visitEntityRoot.get(SPECIALIZATION), visitSearchCriteria.getSpecializationType());
            predicates.add(specializationTypePredicate);
        }
        if (visitSearchCriteria.getPatientFirstName() != null) {
            Join<VisitEntity, PatientEntity> patient = visitEntityRoot.join(PATIENT);
            Predicate patientFirstNamePredicate = builder.equal(patient.get(FIRST_NAME), visitSearchCriteria.getPatientFirstName());
            predicates.add(patientFirstNamePredicate);
        }
        if (visitSearchCriteria.getPatientLastName() != null) {
            Join<VisitEntity, PatientEntity> patient = visitEntityRoot.join(PATIENT);
            Predicate patientLastNamePredicate = builder.equal(patient.get(LAST_NAME), visitSearchCriteria.getPatientLastName());
            predicates.add(patientLastNamePredicate);
        }

        Predicate[] predicate = predicates.toArray(new Predicate[0]);
        criteriaQuery.where(predicate);
        TypedQuery<VisitEntity> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<VisitEntity> findAllVisitsFromLastMonthWithStatusUnrealizedPatientByPatientId(SpecializationType specializationType, Long patientId) {
        QVisitEntity visitEntity = QVisitEntity.visitEntity;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        LocalDate localDateNow = LocalDate.now().minusMonths(MONTHS_TO_SUBTRACT);

        return jpaQueryFactory.selectFrom(visitEntity)
                .where(visitEntity.patient.id.eq(patientId),
                        visitEntity.visitStatus.eq(VisitStatus.UNREALIZED_PATIENT),
                        visitEntity.visitDate.after(localDateNow.atStartOfDay()),
                        visitEntity.specialization.eq(specializationType))
                .fetch();
    }

    @Override
    public SpecializationType findSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized() {
        QVisitEntity visitEntity = QVisitEntity.visitEntity;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        List<SpecializationType> specializationTypes =
                jpaQueryFactory.select(visitEntity.specialization).from(visitEntity)
                .where(visitEntity.visitStatus.eq(VisitStatus.UNREALIZED_PATIENT)
                        .or(visitEntity.visitStatus.eq(VisitStatus.UNREALIZED_OTHERS)))
                .groupBy(visitEntity.specialization)
                .orderBy(visitEntity.id.count().desc())
                .fetch();

        if (specializationTypes.isEmpty()) {
            throw new NotFoundException(NO_SPECIALIZATION_TYPE_WITH_STATUS_UNREALIZED);
        }

        return specializationTypes.get(0);
    }

    @Override
    public BigDecimal calculateTotalVisitCostByPatientAndDate(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        if (patientId == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException();
        }
        TypedQuery<BigDecimal> query = entityManager.createNamedQuery(VisitEntity.QUERY_CALCULATE_TOTAL_VISIT_COST_BY_PATIENT_AND_DATE, BigDecimal.class);
        query.setParameter(PATIENT_ID, patientId);
        query.setParameter(START_DATE, startDate);
        query.setParameter(END_DATE, endDate);
        return query.getSingleResult();
    }
}
