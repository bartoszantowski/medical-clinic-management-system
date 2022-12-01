package com.iitrab.dao;

import com.iitrab.domain.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface DoctorRepo extends JpaRepository<DoctorEntity, Long>, DoctorRepoCustom {

    /**
     * Query checks whether the given doctor can see the patient on the given date.
     * Checks whether the given date is not already taken.
     *
     * @param doctorId doctor Id
     * @param date date
     * @return doctor or null
     */
    @Query("SELECT d FROM DoctorEntity d LEFT JOIN d.visits v WHERE d.id = :doctorId AND v.visitDate IS NULL OR v.visitDate <> :date")
    DoctorEntity checkIfDoctorIsAvailableOnThisDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDateTime date);
}
