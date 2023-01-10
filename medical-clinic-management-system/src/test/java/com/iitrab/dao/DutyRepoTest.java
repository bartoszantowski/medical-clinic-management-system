package com.iitrab.dao;

import com.iitrab.AbstractTest;
import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.DutyEntity;
import com.iitrab.domain.SpecializationEntity;
import com.iitrab.domain.SpecializationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;

import static com.iitrab.SampleTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DutyRepoTest extends AbstractTest {

    @Autowired
    private DutyRepo dutyRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private SpecializationRepo specializationRepo;

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingDuty_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime date = LocalDateTime.of(2022, 11, 10, 12, 0);

        DutyEntity dutyEntity = dutyEntity(doctor, date, date.plusHours(1));
        dutyEntity = dutyRepo.save(dutyEntity);
        dutyEntity.setEndDutyDate(date.plusHours(2));
        dutyRepo.save(dutyEntity);

        // when && then
        DutyEntity finalDutyEntity = dutyEntity;
        assertThatThrownBy(() -> dutyRepo.save(finalDutyEntity)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnDutyWithCreatedDateAndUpdated_whenCreatingAndUpdating() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        LocalDateTime date = LocalDateTime.of(2022, 11, 10, 12, 0);

        DutyEntity dutyEntity = dutyEntity(doctor, date, date.plusHours(1));

        // when
        dutyEntity = dutyRepo.save(dutyEntity);
        LocalDateTime localDateTime = LocalDateTime.now();
        dutyEntity = dutyRepo.save(dutyEntity);

        // && then
        assertThat(dutyEntity.getCreateDate()).isAfter(localDateTime.minusSeconds(5));
        assertThat(dutyEntity.getCreateDate()).isBeforeOrEqualTo(dutyEntity.getUpdateDate());
    }
}