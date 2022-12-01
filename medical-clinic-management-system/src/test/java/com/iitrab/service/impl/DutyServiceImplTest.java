package com.iitrab.service.impl;

import com.iitrab.AbstractTest;
import com.iitrab.dao.DoctorRepo;
import com.iitrab.dao.DutyRepo;
import com.iitrab.dao.SpecializationRepo;
import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.DutyEntity;
import com.iitrab.domain.SpecializationEntity;
import com.iitrab.domain.SpecializationType;
import com.iitrab.exception.api.BusinessException;
import com.iitrab.exception.api.DateTimeException;
import com.iitrab.types.CustomDutyTO;
import com.iitrab.types.DutyTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;

import static com.iitrab.SampleTestDataFactory.doctorEntity;
import static com.iitrab.SampleTestDataFactory.specializationEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DutyServiceImplTest extends AbstractTest {

    @Autowired
    private DutyServiceImpl dutyService;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @Autowired
    private DutyRepo dutyRepo;

    @Test
    void shouldReturnAddedDuty_whenAddingDuty() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 18, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when
        DutyTO expected = dutyService.addDoctorsDuty(customDutyTO);
        DutyEntity expectedDuty = dutyRepo.findAll().get(0);

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getDoctorFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(expected.getDoctorLastName()).isEqualTo(doctor.getLastName());
        assertThat(expected.getStartDutyDate()).isEqualTo(customDutyTO.getStartDutyDate());
        assertThat(expected.getEndDutyDate()).isEqualTo(customDutyTO.getEndDutyDate());
        assertThat(expectedDuty.getDoctor().getId()).isEqualTo(doctor.getId());
    }

    @Test
    void shouldReturnBusinessException_whenAddingDuty_andDoctorIdIsNull() {
        // given
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 10, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 14, 0);
        CustomDutyTO customDutyTO = new CustomDutyTO(null, startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenAddingDuty_andStartDutyDateIsNull() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 14, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), null, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenAddingDuty_andEndDutyDateIsNull() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 10, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, null);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andStartDateIsAfterEndDutyDate() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 10, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 9, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andStartDutyDateIsNotWorkingDay() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 13, 10, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 14, 14, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andEndDutyDateIsNotWorkingDay() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 11, 10, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 12, 14, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andStartDutyDateIsNotWorkingTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 7, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 14, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andEndDutyDateIsNotWorkingTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 12, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 19, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andStartDutyDateIsIncorrectIntervalTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 12, 14);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 17, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andStartDutyDateIsIncorrectIntervalTime2() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 8, 1);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 17, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andEndDutyDateIsIncorrectIntervalTime() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 12, 15);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 17, 59);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }

    @Test
    void shouldReturnDateTimeException_whenAddingDuty_andEndDutyDateIsIncorrectIntervalTime2() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 12, 15);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 18, 1);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                DateTimeException.class);
    }


    @Test
    void shouldReturnBusinessException_whenAddingDuty_andDoctorDoesNotExist() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        doctorRepo.deleteById(doctor.getId());
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 12, 15);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 17, 45);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturnBusinessException_whenAddingDuty_andIsDutyConflict() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty1 = LocalDateTime.of(2022, 11, 10, 12, 0);
        LocalDateTime endDuty1 = LocalDateTime.of(2022, 11, 10, 13, 45);
        CustomDutyTO customDutyTO1 = new CustomDutyTO(doctor.getId(), startDuty1, endDuty1);
        dutyService.addDoctorsDuty(customDutyTO1);

        LocalDateTime startDuty2 = LocalDateTime.of(2022, 11, 10, 13, 0);
        LocalDateTime endDuty2 = LocalDateTime.of(2022, 11, 10, 16, 30);
        CustomDutyTO customDutyTO2 = new CustomDutyTO(doctor.getId(), startDuty2, endDuty2);

        // when && then
        assertThatThrownBy(() -> dutyService.addDoctorsDuty(customDutyTO2)).isInstanceOf(
                BusinessException.class);
    }

    @Test
    void shouldReturn2Duties_whenAddingDuties_andAdded2Duties_withEndTimeFirstIsStartTimeSecond() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty1 = LocalDateTime.of(2022, 11, 10, 12, 0);
        LocalDateTime endDuty1 = LocalDateTime.of(2022, 11, 11, 14, 0);
        CustomDutyTO customDutyTO1 = new CustomDutyTO(doctor.getId(), startDuty1, endDuty1);

        LocalDateTime startDuty2 = LocalDateTime.of(2022, 11, 10, 14, 0);
        LocalDateTime endDuty2 = LocalDateTime.of(2022, 11, 10, 16, 15);
        CustomDutyTO customDutyTO2 = new CustomDutyTO(doctor.getId(), startDuty2, endDuty2);

        // when
        dutyService.addDoctorsDuty(customDutyTO1);
        dutyService.addDoctorsDuty(customDutyTO2);

        // then
        List<DutyEntity> expectedDuties = dutyRepo.findAll();
        assertThat(expectedDuties).hasSize(2);
    }

    @Test
    void shouldDeleteDuty_whenDeletingDuty_andNoDeleteDoctor() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        doctor = doctorRepo.save(doctor);
        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 18, 0);

        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);

        dutyService.addDoctorsDuty(customDutyTO);
        DutyEntity expected = dutyRepo.findAll().get(0);

        // when
        dutyRepo.deleteById(expected.getId());

        // then
        DoctorEntity expectedDoctor = doctorRepo.findAll().get(0);
        List<DutyEntity> expectedDuties = dutyRepo.findAll();

        assertThat(expectedDoctor).isNotNull();
        assertThat(expectedDoctor.getId()).isEqualTo(doctor.getId());
        assertThat(expectedDoctor.getDuties()).isEmpty();
        assertThat(expectedDuties).isEmpty();
    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingDuty_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime startDuty = LocalDateTime.of(2022, 11, 10, 8, 0);
        LocalDateTime endDuty = LocalDateTime.of(2022, 11, 10, 18, 0);
        CustomDutyTO customDutyTO = new CustomDutyTO(doctor.getId(), startDuty, endDuty);
        dutyService.addDoctorsDuty(customDutyTO);

        DutyEntity expected = dutyRepo.findAll().get(0);
        expected.setEndDutyDate(endDuty.minusMinutes(15));
        dutyRepo.save(expected);

        // when && then
        assertThatThrownBy(() -> dutyRepo.save(expected)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }
}