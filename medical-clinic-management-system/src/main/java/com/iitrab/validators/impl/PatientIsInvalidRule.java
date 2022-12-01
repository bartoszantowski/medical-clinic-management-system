package com.iitrab.validators.impl;

import com.iitrab.dao.PatientRepo;
import com.iitrab.dao.SpecializationRepo;
import com.iitrab.dao.VisitRepo;
import com.iitrab.domain.*;
import com.iitrab.exception.api.BusinessException;
import com.iitrab.exception.api.NotFoundException;
import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.validators.VisitValidatorByDoctorRule;
import com.iitrab.validators.VisitValidatorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for patient validation.
 * Checks of the compliance of the given patient with applicable regulations.
 */
@Order(300)
@Component
@Slf4j
public class PatientIsInvalidRule  implements VisitValidatorRule, VisitValidatorByDoctorRule {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    private static final String PATIENT_WITH_ID = "Patient with id:";
    private static final String CAN_T_SCHEDULE_VISIT_TO_DOCTOR_WITH_SPECIALIZATION = " can't schedule visit to doctor with specialization: ";
    private static final String BLOCKED_DUE_TO_AN_UNREALIZED_VISIT_DUE_TO_THE_PATIENT_S_FAULT = " -> blocked due to an unrealized visit due to the patient's fault!";
    private static final String CAN_T_SCHEDULE_VISIT_TO_PEDIATRICIAN = " can't schedule visit to pediatrician!";
    private static final String CAN_T_SCHEDULE_VISIT_TO_INTERNIST = " can't schedule visit to internist!";
    private static final String CAN_T_SCHEDULE_VISIT_TO_GYNECOLOGIST = " can't schedule visit to gynecologist!";
    private static final String CAN_T_SCHEDULE_VISIT_TO_GERIATRICIAN = " can't schedule visit to geriatrician!";
    private static final int ADULT_AGE = 18;
    private static final int OLD_AGE = 65;
    private static final String DOCTOR_WITH_SPECIALIZATION = "Doctor with specialization: ";
    private static final String IS_NOT_AVAILABLE = " is not available!!!";
    private static final String DOESN_T_EXIST = " doesn't exist!!!";

    private final List<SpecializationType> validatedSpecializationsByPatient =
            List.of(SpecializationType.PEDIATRICIAN, SpecializationType.INTERNIST, SpecializationType.GYNECOLOGIST, SpecializationType.GERIATRICIAN);

    private final List<SpecializationType> noValidatedSpecializationsByVisitStatus =
            List.of(SpecializationType.PEDIATRICIAN, SpecializationType.INTERNIST);

    @Override
    public boolean applyBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        checkBySpecialization(customVisitSpecializationTO);
        return true;
    }

    @Override
    public boolean applyByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        checkByDoctor(customVisitDoctorAndSpecializationTO);
        return true;
    }

    private void checkByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        ifPatientExist(customVisitDoctorAndSpecializationTO.getPatientId());
        ifSpecializationIsAvailable(customVisitDoctorAndSpecializationTO.getDoctorSpecializationType());
        ifPatientScheduleVisitToThisSpecialist(customVisitDoctorAndSpecializationTO.getDoctorSpecializationType(), customVisitDoctorAndSpecializationTO.getPatientId());
        ifPatientScheduleVisitToThisSpecialistByLastVisitStatus(customVisitDoctorAndSpecializationTO.getDoctorSpecializationType(), customVisitDoctorAndSpecializationTO.getPatientId());
    }

    private void checkBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        ifPatientExist(customVisitSpecializationTO.getPatientId());
        ifSpecializationIsAvailable(customVisitSpecializationTO.getDoctorSpecializationType());
        ifPatientScheduleVisitToThisSpecialist(customVisitSpecializationTO.getDoctorSpecializationType(), customVisitSpecializationTO.getPatientId());
        ifPatientScheduleVisitToThisSpecialistByLastVisitStatus(customVisitSpecializationTO.getDoctorSpecializationType(), customVisitSpecializationTO.getPatientId());
    }

    private void ifPatientScheduleVisitToThisSpecialistByLastVisitStatus(SpecializationType specializationType, Long patientId) {
        if (!noValidatedSpecializationsByVisitStatus.contains(specializationType)) {
            validatedSpecializationsByLastVisitStatus(specializationType, patientId);
        }
    }

    private void validatedSpecializationsByLastVisitStatus(SpecializationType specializationType, Long patientId) {
        List<VisitEntity> visits = visitRepo.findAllVisitsFromLastMonthWithStatusUnrealizedPatientByPatientId(specializationType, patientId);

        if (visits.size() >= 2) {
            throw new BusinessException(
                    PATIENT_WITH_ID + patientId +
                            CAN_T_SCHEDULE_VISIT_TO_DOCTOR_WITH_SPECIALIZATION +
                            specializationType +
                            BLOCKED_DUE_TO_AN_UNREALIZED_VISIT_DUE_TO_THE_PATIENT_S_FAULT);
        }
    }

    /**
     * PEDIATRICIAN -> only <18 y/o
     * INTERNIST -> only >18 y/o
     * GYNECOLOGIST -> only women
     * GERIATRICIAN -> only >65 y/o
     *
     * @param specializationType specialization type
     * @param patientId patient
     */
    private void ifPatientScheduleVisitToThisSpecialist(SpecializationType specializationType, Long patientId) {
        if (validatedSpecializationsByPatient.contains(specializationType)) {
            checkIfVisitIsPossible(specializationType, patientId);
        }
    }

    private void checkIfVisitIsPossible(SpecializationType specializationType, Long patientId) {
        PatientEntity patient = patientRepo.getOne(patientId);

        switch (specializationType) {
            case PEDIATRICIAN:
                if (Period.between(patient.getBirthDate(), LocalDate.now()).getYears() >= ADULT_AGE) {
                    throw new BusinessException(PATIENT_WITH_ID + patient.getId() + CAN_T_SCHEDULE_VISIT_TO_PEDIATRICIAN);
                }
                break;

            case INTERNIST:
                if (Period.between(patient.getBirthDate(), LocalDate.now()).getYears() < ADULT_AGE) {
                    throw new BusinessException(PATIENT_WITH_ID + patient.getId() + CAN_T_SCHEDULE_VISIT_TO_INTERNIST);
                }
                break;

            case GYNECOLOGIST:
                if (patient.getSex().equals(Sex.MALE)) {
                    throw new BusinessException(PATIENT_WITH_ID + patient.getId() + CAN_T_SCHEDULE_VISIT_TO_GYNECOLOGIST);
                }
                break;

            case GERIATRICIAN:
                if (Period.between(patient.getBirthDate(), LocalDate.now()).getYears() < OLD_AGE) {
                    throw new BusinessException(PATIENT_WITH_ID + patient.getId() + CAN_T_SCHEDULE_VISIT_TO_GERIATRICIAN);
                }
                break;
        }
    }

    private void ifSpecializationIsAvailable(SpecializationType doctorSpecializationType) {
        List<SpecializationEntity> specializations = specializationRepo.findAll();
        
        List<SpecializationEntity> availableSpecializations = specializations.stream()
                .filter(specializationEntity ->
                        specializationEntity.getSpecializationType().equals(doctorSpecializationType) &&
                                !specializationEntity.getDoctors().isEmpty())
                .collect(Collectors.toList());
        
        if (availableSpecializations.isEmpty()) {
            throw new NotFoundException(DOCTOR_WITH_SPECIALIZATION + doctorSpecializationType + IS_NOT_AVAILABLE);
        }

    }
    
    private void ifPatientExist(Long patientId) {
        if (!patientRepo.existsById(patientId)) {
            throw new NotFoundException(PATIENT_WITH_ID + patientId + DOESN_T_EXIST);
        }
    }
}
