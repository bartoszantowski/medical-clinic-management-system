package com.iitrab.dao.impl;

import com.iitrab.dao.DoctorRepoCustom;
import com.iitrab.domain.*;
import com.iitrab.exception.api.NotFoundException;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorRepoCustomImpl implements DoctorRepoCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private static final int INTERVAL_TIME_QUARTER = 15;
    private static final String DATE_FROM = "dateFrom";
    private static final String DATE_TO = "dateTo";
    private static final String NO_DOCTORS_AVAILABLE_AT_THE_GIVEN_DATE = "No doctors available at the given date!";
    private static final LocalTime START_OF_THE_DAY = LocalTime.of(0, 0);
    private static final LocalTime END_OF_THE_DAY = LocalTime.of(23, 59);

    @Override
    public List<DoctorEntity> findAllDoctorsWithAvailableVisitDateBySpecializationAndDate(LocalDate date, SpecializationType specialization) {
        QVisitEntity visitEntity = QVisitEntity.visitEntity;
        QDutyEntity dutyEntity = QDutyEntity.dutyEntity;
        QDoctorEntity doctorEntity = QDoctorEntity.doctorEntity;
        QSpecializationEntity specializationEntity = QSpecializationEntity.specializationEntity;

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        LocalDateTime start = date.atTime(START_OF_THE_DAY);
        LocalDateTime end = date.atTime(END_OF_THE_DAY);

        List<DutyEntity> duties = jpaQueryFactory.select(dutyEntity).from(doctorEntity)
                .join(doctorEntity.specializations, specializationEntity)
                .join(doctorEntity.duties, dutyEntity)
                .where(specializationEntity.specializationType.eq(specialization))
                .where(dutyEntity.startDutyDate.between(start, end).or(dutyEntity.endDutyDate.between(start, end)))
                .fetch();

        Map<DoctorEntity, Long> doctorAndNumberOfPossibleVisits = createDoctorAndNumberOfPossibleVisitsMap(duties);

        List<VisitEntity> visits = jpaQueryFactory.select(visitEntity).from(doctorEntity)
                .join(doctorEntity.specializations, specializationEntity)
                .join(doctorEntity.duties, dutyEntity)
                .leftJoin(doctorEntity.visits, visitEntity)
                .where(specializationEntity.specializationType.eq(specialization))
                .where(dutyEntity.startDutyDate.between(start, end).or(dutyEntity.endDutyDate.between(start, end)))
                .where(visitEntity.visitDate.between(start, end))
                .fetch();

        Map<Long, Long> numberOfVisits = createDoctorIdAndNumberOfVisitsMap(visits);

        List<DoctorEntity> availableDoctors = createAvailableDoctorsList(doctorAndNumberOfPossibleVisits, numberOfVisits);

        if (availableDoctors.isEmpty()) {
            generateException();
        }
        return availableDoctors;
    }

    @Override
    public DoctorEntity findAllDoctorsBySpecializationAndAvailableThatDate(CustomVisitSpecializationTO customVisitSpecializationTO) {
        QVisitEntity visitEntity = QVisitEntity.visitEntity;
        QDutyEntity dutyEntity = QDutyEntity.dutyEntity;
        QDoctorEntity doctorEntity = QDoctorEntity.doctorEntity;
        QSpecializationEntity specializationEntity = QSpecializationEntity.specializationEntity;

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        List<DoctorEntity> availableDoctors = jpaQueryFactory.selectFrom(doctorEntity)
                .join(doctorEntity.specializations, specializationEntity)
                .join(doctorEntity.duties, dutyEntity)
                .leftJoin(doctorEntity.visits, visitEntity)
                .where(specializationEntity.specializationType.eq(customVisitSpecializationTO.getDoctorSpecializationType()))
                .where(dutyEntity.startDutyDate.before(customVisitSpecializationTO.getVisitDate()))
                .where(dutyEntity.endDutyDate.after(customVisitSpecializationTO.getVisitDate()))
                .where(visitEntity.visitDate.isNull().or(visitEntity.visitDate.notIn(customVisitSpecializationTO.getVisitDate())))
                .fetch();

        if (availableDoctors.isEmpty()) {
            generateException();
        }
        return availableDoctors.get(0);
    }

    @Override
    public DoctorEntity findDoctorWithTheFewestNumberOfPatientsBetweenDates(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new IllegalArgumentException();
        }

        TypedQuery<DoctorEntity> query = entityManager.createNamedQuery(DoctorEntity.QUERY_FIND_DOCTOR_WITH_THE_FEWEST_NUMBER_OF_PATIENTS_BETWEEN_DATES, DoctorEntity.class).setMaxResults(1);
        query.setParameter(DATE_FROM, dateFrom);
        query.setParameter(DATE_TO, dateTo);
        return query.getSingleResult();
    }

    @Override
    public List<DoctorEntity> findTwoDoctorsWithTheHighestNumberOfVisit() {
        TypedQuery<DoctorEntity> query = entityManager.createNamedQuery(DoctorEntity.QUERY_FIND_TWO_DOCTORS_WITH_THE_HIGHEST_NUMBER_OF_VISIT, DoctorEntity.class).setMaxResults(2);

        return query.getResultList();
    }

    private void generateException() {
        throw new NotFoundException(NO_DOCTORS_AVAILABLE_AT_THE_GIVEN_DATE);
    }

    private List<DoctorEntity> createAvailableDoctorsList(Map<DoctorEntity, Long> doctorAndNumberOfPossibleVisits, Map<Long, Long> numberOfVisits) {
        if (doctorAndNumberOfPossibleVisits.isEmpty() || numberOfVisits.isEmpty()) {
            generateException();
        }

        List<DoctorEntity> availableDoctors = new ArrayList<>();

        for (Map.Entry<DoctorEntity, Long> entry : doctorAndNumberOfPossibleVisits.entrySet()) {
            if (!numberOfVisits.containsKey(entry.getKey().getId())) {
                availableDoctors.add(entry.getKey());
            }
            else {
                if (entry.getValue() - numberOfVisits.get(entry.getKey().getId()) > 0) {
                    availableDoctors.add(entry.getKey());
                }
            }
        }
        return availableDoctors;
    }

    private Map<Long, Long> createDoctorIdAndNumberOfVisitsMap(List<VisitEntity> visits) {
        if (visits.isEmpty()) {
            generateException();
        }

        Map<Long, Long> numberOfVisits = new HashMap<>();
        for (VisitEntity visit : visits) {
            numberOfVisits.merge(visit.getDoctor().getId(), 1L, Long::sum);
        }
        return numberOfVisits;
    }

    private Map<DoctorEntity, Long> createDoctorAndNumberOfPossibleVisitsMap(List<DutyEntity> duties) {
        if (duties.isEmpty()) {
            generateException();
        }

        Map<DoctorEntity, Long> doctorAndNumberOfPossibleVisits = new HashMap<>();
        for (DutyEntity duty : duties) {
            Long maxNumberOfVisits = (Duration.between(duty.getStartDutyDate(), duty.getEndDutyDate()).toMinutes()) / INTERVAL_TIME_QUARTER;
            doctorAndNumberOfPossibleVisits.put(duty.getDoctor(), maxNumberOfVisits);
        }
        return doctorAndNumberOfPossibleVisits;
    }
}
