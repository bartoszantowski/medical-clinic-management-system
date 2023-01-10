package com.iitrab.service.impl;

import com.iitrab.AbstractTest;
import com.iitrab.dao.*;
import com.iitrab.domain.*;
import com.iitrab.exception.api.BusinessException;
import com.iitrab.exception.api.DateTimeException;
import com.iitrab.exception.api.NotFoundException;
import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.types.VisitTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.iitrab.SampleTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VisitServiceImplTest extends AbstractTest {

    @Autowired
    private VisitServiceImpl visitService;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @Autowired
    private DutyRepo dutyRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Test
    void shouldReturnAddedVisit_whenSchedulingVisitBySpecialization() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        // when
        VisitTO expected = visitService.scheduleVisitBySpecialization(customVisitSpecializationTO);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getPatientId()).isEqualTo(patient.getId());
        assertThat(expected.getVisitDate()).isEqualTo(visitDate);
        assertThat(expected.getDoctorLastName()).isEqualTo(doctor.getLastName());
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andCustomVisitSpecializationTOIsNull() {
        // given

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(null)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientIdIsNull() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(null, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andSpecializationTypeIsNull() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, null, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andVisitDateIsNull() {
        // given
        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, null);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitBySpecialization_andVisitDateIsNotWorkingDay() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 20, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitBySpecialization_andVisitDateIsNotWorkingTime() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 7, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitBySpecialization_andVisitDateIsIsIncorrectIntervalTime() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 8, 59);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitBySpecialization_andVisitDateIsIsIncorrectIntervalTime2() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 1);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnNotFoundException_whenSchedulingVisitBySpecialization_andPatientDoesNotExist() {
        // given
        //PatientEntity patient = patientEntity();
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                NotFoundException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andNoSpecializationIsNotAvailable() {
        // given
        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andSpecializationIsNotAvailable() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.PEDIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_PEDIATRICIAN() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.PEDIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.PEDIATRICIAN, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_INTERNIST() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient.setBirthDate(LocalDate.of(2020, 1, 1));
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.INTERNIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_GYNECOLOGIST() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.GYNECOLOGIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.GYNECOLOGIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_GERIATRICIAN() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.GERIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.GERIATRICIAN, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientIsBlockedByUnrealizedLastVisitStatus() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 12, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 12, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 12, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate.minusDays(7));
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusDays(7));
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.DENTIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit2);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andPatientIsBlockedByUnrealizedLastVisitStatus_ButItIsInternist() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.INTERNIST, visitDate);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate.minusDays(7));
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusDays(7));
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.DENTIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit2);

        // when
        VisitTO expected = visitService.scheduleVisitBySpecialization(customVisitSpecializationTO);

        // then
        assertThat(expected.getSpecializationType()).isEqualTo(customVisitSpecializationTO.getDoctorSpecializationType());
        assertThat(expected.getVisitDate()).isEqualTo(customVisitSpecializationTO.getVisitDate());
        assertThat(expected.getPatientId()).isEqualTo(customVisitSpecializationTO.getPatientId());
        assertThat(expected.getDoctorLastName()).isEqualTo(doctor.getLastName());
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andNoDoctorOnDuty() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 13, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andDoctorOnDutyHasOtherSpecialization() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);
        LocalDateTime startDuty2 = LocalDateTime.of(2022, 11, 22, 13, 0);
        LocalDateTime endDuty2 = LocalDateTime.of(2022, 11, 22, 17, 0);
        DutyEntity dutyEntity2 = dutyEntity(doctor2, startDuty2, endDuty2);
        dutyRepo.save(dutyEntity2);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 13, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andDoctorEndDuty() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 12, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.OCULIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitBySpecialization_andDoctorHasVisitInThisTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 0);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.OCULIST, visitDate);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitBySpecialization(customVisitSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldDeleteVisit_whenDeletingVisit_cascadeTest() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visit1 = visitRepo.save(visit1);

        // when
        visitRepo.deleteById(visit1.getId());

        // then
        List<VisitEntity> visitEntities = visitRepo.findAll();
        List<DoctorEntity> doctorEntity = doctorRepo.findAll();
        List<SpecializationEntity> specializationEntities = specializationRepo.findAll();
        List<PatientEntity> patientEntities = patientRepo.findAll();

        assertThat(visitEntities).isEmpty();

        assertThat(doctorEntity).hasSize(1);
        assertThat(doctorEntity.get(0).getId()).isEqualTo(doctor.getId());

        assertThat(specializationEntities).hasSize(1);
        assertThat(specializationEntities.get(0).getId()).isEqualTo(specializationEntity.getId());

        assertThat(patientEntities).hasSize(1);
        assertThat(patientEntities.get(0).getId()).isEqualTo(patient.getId());

    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingVisit_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitSpecializationTO customVisitSpecializationTO =
                new CustomVisitSpecializationTO(patient.getId(), SpecializationType.DENTIST, visitDate);

        visitService.scheduleVisitBySpecialization(customVisitSpecializationTO);

        VisitEntity expected = visitRepo.findAll().get(0);
        expected.setVisitStatus(VisitStatus.UNREALIZED_OTHERS);
        visitRepo.save(expected);

        // when && then
        assertThatThrownBy(() -> visitRepo.save(expected)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);

    }

    @Test
    void shouldReturnAddedVisit_whenSchedulingVisitByDoctorAndSpecialization() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO custom =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.DENTIST, visitDate);

        // when
        VisitTO expected = visitService.scheduleVisitByDoctorAndSpecialization(custom);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getPatientId()).isEqualTo(patient.getId());
        assertThat(expected.getVisitDate()).isEqualTo(visitDate);
        assertThat(expected.getDoctorLastName()).isEqualTo(doctor.getLastName());
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andCustomVisitDoctorAndSpecializationTOIsNull() {
        // given

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(null)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientIdIsNull() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(null,55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDocotrIdIsNull() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L,null, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andSpecializationTypeIsNull() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L,66L, null, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andVisitDateIsNull() {
        // given
        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L, 21L, SpecializationType.DENTIST, null);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitByDoctorAndSpecialization_andVisitDateIsNotWorkingDay() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 20, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L, 88L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitByDoctorAndSpecialization_andVisitDateIsNotWorkingTime() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 7, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(88L, 55L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitByDoctorAndSpecialization_andVisitDateIsIsIncorrectIntervalTime() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 8, 59);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L, 88L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenSchedulingVisitByDoctorAndSpecialization_andVisitDateIsIsIncorrectIntervalTime2() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 1);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L, 88L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnNotFoundException_whenSchedulingVisitByDoctorAndSpecialization_andPatientDoesNotExist() {
        // given
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(55L, 82L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                NotFoundException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andNoSpecializationIsNotAvailable() {
        // given
        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(),82L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andSpecializationIsNotAvailable() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.PEDIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_PEDIATRICIAN() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.PEDIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.PEDIATRICIAN, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_INTERNIST() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient.setBirthDate(LocalDate.of(2020, 1, 1));
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.INTERNIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_GYNECOLOGIST() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.GYNECOLOGIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.GYNECOLOGIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientCanNotScheduleToDoctorWithThisSpecialization_GERIATRICIAN() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.GERIATRICIAN);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 17, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.GERIATRICIAN, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientIsBlockedByUnrealizedLastVisitStatus() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.DENTIST, visitDate);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate.minusDays(7));
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusDays(7));
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.DENTIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit2);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andPatientIsBlockedByUnrealizedLastVisitStatus_ButItIsInternist() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.INTERNIST, visitDate);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate.minusDays(7));
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusDays(7));
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.DENTIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit2);

        // when
        VisitTO expected = visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO);

        // then
        assertThat(expected.getSpecializationType()).isEqualTo(customVisitDoctorAndSpecializationTO.getDoctorSpecializationType());
        assertThat(expected.getVisitDate()).isEqualTo(customVisitDoctorAndSpecializationTO.getVisitDate());
        assertThat(expected.getPatientId()).isEqualTo(customVisitDoctorAndSpecializationTO.getPatientId());
        assertThat(expected.getDoctorLastName()).isEqualTo(doctor.getLastName());
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andNoDoctorOnDuty() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.INTERNIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 13, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);

    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorOnDutyHasOtherSpecialization() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.DENTIST);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity, specializationEntity2));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);
        LocalDateTime startDuty2 = LocalDateTime.of(2022, 11, 22, 13, 0);
        LocalDateTime endDuty2 = LocalDateTime.of(2022, 11, 22, 17, 0);
        DutyEntity dutyEntity2 = dutyEntity(doctor2, startDuty2, endDuty2);
        dutyRepo.save(dutyEntity2);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 13, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorEndDuty() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 12, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyEntity = dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 12, 0);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), 82L, SpecializationType.OCULIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorNoExist() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        doctorRepo.deleteById(doctor.getId());

        CustomVisitDoctorAndSpecializationTO custom =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(custom)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorDoesNotHaveThisSpecialization() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO custom =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.OCULIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(custom)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorIsNotOnDutyInThisTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 10, 15);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO custom =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(custom)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenSchedulingVisitByDoctorAndSpecialization_andDoctorIsNotAvailableInThisTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 15, 15);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visitRepo.save(visit1);

        CustomVisitDoctorAndSpecializationTO custom =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.DENTIST, visitDate);

        // when && then
        assertThatThrownBy(() -> visitService.scheduleVisitByDoctorAndSpecialization(custom)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingVisitByDoctor_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 22, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 22, 18, 0);
        DutyEntity dutyEntity = dutyEntity(doctor, startDuty, endDuty);
        dutyRepo.save(dutyEntity);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 15);

        CustomVisitDoctorAndSpecializationTO customVisitDoctorAndSpecializationTO =
                new CustomVisitDoctorAndSpecializationTO(patient.getId(), doctor.getId(), SpecializationType.DENTIST, visitDate);

        visitService.scheduleVisitByDoctorAndSpecialization(customVisitDoctorAndSpecializationTO);

        VisitEntity expected = visitRepo.findAll().get(0);
        expected.setVisitStatus(VisitStatus.UNREALIZED_OTHERS);
        visitRepo.save(expected);

        // when && then
        assertThatThrownBy(() -> visitRepo.save(expected)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }
}