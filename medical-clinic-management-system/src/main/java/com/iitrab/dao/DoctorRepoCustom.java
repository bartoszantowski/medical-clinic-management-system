package com.iitrab.dao;

import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.SpecializationType;
import com.iitrab.types.CustomVisitSpecializationTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DoctorRepoCustom {

    /**
     * Query finds all doctors who have the given specialization type
     * and can see the patient on the given date
     * (they are on duty and have no other visit at that time)
     *
     * @param customVisitSpecializationTO value to check
     * @return all matching doctors
     */
    DoctorEntity findAllDoctorsBySpecializationAndAvailableThatDate(CustomVisitSpecializationTO customVisitSpecializationTO);

    /**
     * Query finds the doctor with the fewest number of patients between the given dates
     * (significant number of specific patients, not visits).
     *
     * @param dateFrom date from
     * @param dateTo date to
     * @return matching doctor or exception
     */
    DoctorEntity findDoctorWithTheFewestNumberOfPatientsBetweenDates(LocalDateTime dateFrom, LocalDateTime dateTo);

    /**
     * Query finds 2 doctors with the highest number of visits.
     *
     * @return List with 2 doctors
     */
    List<DoctorEntity> findTwoDoctorsWithTheHighestNumberOfVisit();

    /**
     * Query finds all doctors of the given specialization who can see the patient on the given day.
     * Doctors are on duty and have free dates of visit.
     *
     * @param date date
     * @param specialization specialization type
     * @return list of available doctors or exception
     */
    List<DoctorEntity> findAllDoctorsWithAvailableVisitDateBySpecializationAndDate(LocalDate date, SpecializationType specialization);
}
