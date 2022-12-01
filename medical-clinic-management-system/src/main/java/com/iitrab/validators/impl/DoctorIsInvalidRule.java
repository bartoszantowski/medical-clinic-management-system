package com.iitrab.validators.impl;

import com.iitrab.dao.DoctorRepo;
import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.DutyEntity;
import com.iitrab.domain.SpecializationEntity;
import com.iitrab.exception.api.BusinessException;
import com.iitrab.exception.api.NotFoundException;
import com.iitrab.types.CustomDutyTO;
import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.validators.DutyValidatorRule;
import com.iitrab.validators.VisitValidatorByDoctorRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for doctor validation.
 * Checks the compliance of the given doctor with applicable regulations.
 */
@Order(350)
@Component
@Slf4j
public class DoctorIsInvalidRule implements DutyValidatorRule, VisitValidatorByDoctorRule {

    private static final String DOCTOR_WITH_ID = "Doctor with id:";
    private static final String IS_NOT_AVAILABLE_ON = " is not available on: ";
    private static final String HAS_NO_SPECIALIZATION = " has no specialization: ";
    private static final String DOESN_T_EXIST = " doesn't exist!!!";
    private static final String CAN_T_TAKE_THIS_DUTY_THIS_DUTY_HAS_CONFLICT_WITH = " can't take this duty!\nThis duty has conflict with: ";
    private static final String IS_NOT_ON_DUTY_ON = " is not on Duty on: ";

    @Autowired
    private DoctorRepo doctorRepo;

    @Override
    public boolean apply(CustomDutyTO customDutyTO) {
        checkCustomDutyTO(customDutyTO);
        return true;
    }

    @Override
    public boolean applyByDoctor(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        checkCustomVisitDoctorAndSpecializationTO(customVisitDoctorAndSpecializationTO);
        return true;
    }

    private void checkCustomDutyTO(CustomDutyTO customDutyTO) {
        ifDoctorExist(customDutyTO.getDoctorId());
        ifDoctorBeOnDuty(customDutyTO);
    }

    private void checkCustomVisitDoctorAndSpecializationTO(CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO) {
        ifDoctorExist(customVisitDoctorAndSpecializationTO.getDoctorId());
        ifDoctorHasThisSpecialization(customVisitDoctorAndSpecializationTO);
        ifDoctorBeOnDuty(customVisitDoctorAndSpecializationTO);
        ifDoctorIsAvailableOnThisDate(customVisitDoctorAndSpecializationTO);
    }

    private void ifDoctorIsAvailableOnThisDate(CustomVisitDoctorAndSpecializationTO custom) {
        DoctorEntity doctor = doctorRepo.checkIfDoctorIsAvailableOnThisDate(custom.getDoctorId(), custom.getVisitDate());
        if (doctor == null) {
            throw new BusinessException(DOCTOR_WITH_ID + custom.getDoctorId() + IS_NOT_AVAILABLE_ON + custom.getVisitDate());
        }
    }

    private void ifDoctorHasThisSpecialization(CustomVisitDoctorAndSpecializationTO custom) {
        DoctorEntity doctor = doctorRepo.getOne(custom.getDoctorId());

        List<SpecializationEntity> matchingSpecialization =  doctor.getSpecializations().stream()
                .filter(s -> s.getSpecializationType().equals(custom.getDoctorSpecializationType()))
                .collect(Collectors.toList());

        if (matchingSpecialization.isEmpty()) {
            throw new BusinessException(DOCTOR_WITH_ID + doctor.getId() +
                    HAS_NO_SPECIALIZATION + custom.getDoctorSpecializationType());
        }

    }

    private void ifDoctorExist(Long doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            throw new NotFoundException(DOCTOR_WITH_ID + doctorId + DOESN_T_EXIST);
        }
    }

    private void ifDoctorBeOnDuty(CustomDutyTO customDutyTO) {
        DoctorEntity doctor = doctorRepo.getOne(customDutyTO.getDoctorId());
        List<DutyEntity> duties = doctor.getDuties();

        List<DutyEntity> conflictingDuties = duties.stream()
                .filter(duty -> duty.getStartDutyDate().isAfter(customDutyTO.getStartDutyDate()) &&
                        duty.getStartDutyDate().isBefore(customDutyTO.getEndDutyDate()) ||
                        duty.getEndDutyDate().isAfter(customDutyTO.getStartDutyDate()) &&
                        duty.getEndDutyDate().isBefore(customDutyTO.getEndDutyDate()))
                .collect(Collectors.toList());

        if (!conflictingDuties.isEmpty()) {
            throw new BusinessException(DOCTOR_WITH_ID + doctor.getId() + CAN_T_TAKE_THIS_DUTY_THIS_DUTY_HAS_CONFLICT_WITH + conflictingDuties);
        }
    }

    private void ifDoctorBeOnDuty(CustomVisitDoctorAndSpecializationTO custom) {
        DoctorEntity doctor = doctorRepo.getOne(custom.getDoctorId());
        List<DutyEntity> duties = doctor.getDuties();

        List<DutyEntity> matchingDuties = duties.stream()
                .filter(duty -> duty.getStartDutyDate().isBefore(custom.getVisitDate()) &&
                                duty.getEndDutyDate().isAfter(custom.getVisitDate()))
                .collect(Collectors.toList());

        if (matchingDuties.isEmpty()) {
            throw new BusinessException(DOCTOR_WITH_ID + doctor.getId() + IS_NOT_ON_DUTY_ON + custom.getVisitDate());
        }
    }
}
