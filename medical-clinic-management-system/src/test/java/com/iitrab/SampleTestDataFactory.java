package com.iitrab;

import com.iitrab.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SampleTestDataFactory {


    public static DoctorEntity doctorEntity() {
        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName(randomUUID().toString());
        doctor.setLastName(randomUUID().toString());
        doctor.setTelephoneNumber(randomUUID().toString());
        doctor.setAddress(randomUUID().toString());
        doctor.setHourlyRate(new BigDecimal("180.00"));
        doctor.setSpecializations(new ArrayList<>());
        doctor.setVisits(new ArrayList<>());
        doctor.setDuties(new ArrayList<>());

        return doctor;
    }

    public static PatientEntity patientEntity() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName(randomUUID().toString());
        patient.setLastName(randomUUID().toString());
        patient.setTelephoneNumber(randomUUID().toString());
        patient.setAddress(randomUUID().toString());
        patient.setBirthDate(LocalDate.of(1990, 5, 22));
        patient.setSex(Sex.MALE);
        patient.setVisits(List.of());

        return patient;
    }

    public static SpecializationEntity specializationEntity(SpecializationType type) {
        SpecializationEntity specializationEntity = new SpecializationEntity();
        specializationEntity.setSpecializationType(type);
        specializationEntity.setDoctors(List.of());

        return specializationEntity;
    }

    public static DutyEntity dutyEntity(DoctorEntity doctor, LocalDateTime startDuty, LocalDateTime endDuty) {
        DutyEntity dutyEntity = new DutyEntity();
        dutyEntity.setDoctor(doctor);
        dutyEntity.setStartDutyDate(startDuty);
        dutyEntity.setEndDutyDate(endDuty);

        return dutyEntity;
    }
}
