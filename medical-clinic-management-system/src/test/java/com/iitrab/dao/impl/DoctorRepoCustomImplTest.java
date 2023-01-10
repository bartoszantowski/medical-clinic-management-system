package com.iitrab.dao.impl;

import com.iitrab.AbstractTest;
import com.iitrab.dao.*;
import com.iitrab.domain.*;
import com.iitrab.exception.api.BusinessException;
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
class DoctorRepoCustomImplTest extends AbstractTest {

    @Autowired
    private DutyRepo dutyRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private SpecializationRepo specializationRepo;
    
    @Test
    void shouldReturnDoctorWithTheFewestNumberOfPatients_whenFindingDoctorWithTheFewestNumberOfPatients() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        DoctorEntity doctor3 = doctorEntity();
        doctor3.setSpecializations(List.of(specializationEntity));
        doctor3 = doctorRepo.save(doctor3);
        
        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

        PatientEntity patient3 = patientEntity();
        patient3 = patientRepo.save(patient3);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 0);
        
        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusHours(2));
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient2);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate.plusHours(3));
        visit3.setDoctor(doctor2);
        visit3.setPatient(patient3);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate);
        visit4.setDoctor(doctor2);
        visit4.setPatient(patient3);
        visit4.setSpecialization(SpecializationType.SURGEON);
        visitRepo.save(visit4);

        VisitEntity visit7 = new VisitEntity();
        visit7.setVisitDate(visitDate);
        visit7.setDoctor(doctor2);
        visit7.setPatient(patient);
        visit7.setSpecialization(SpecializationType.SURGEON);
        visitRepo.save(visit7);

        VisitEntity visit5 = new VisitEntity();
        visit5.setVisitDate(visitDate.plusDays(4));
        visit5.setDoctor(doctor2);
        visit5.setPatient(patient);
        visit5.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit5);

        VisitEntity visit6 = new VisitEntity();
        visit6.setVisitDate(visitDate);
        visit6.setDoctor(doctor3);
        visit6.setPatient(patient2);
        visit6.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit6);

        // when
        DoctorEntity expected = doctorRepo.findDoctorWithTheFewestNumberOfPatientsBetweenDates(visitDate.minusDays(1), visitDate.plusDays(1));

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(doctor3.getId());
    }

    @Test
    void shouldReturnTwoDoctorsWithTheHighestNumberOfVisit_whenFindingTwoDoctorsWithTheHighestNumberOfVisit() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        DoctorEntity doctor3 = doctorEntity();
        doctor3.setSpecializations(List.of(specializationEntity));
        doctor3 = doctorRepo.save(doctor3);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusHours(2));
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate.plusHours(3));
        visit3.setDoctor(doctor2);
        visit3.setPatient(patient);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate);
        visit4.setDoctor(doctor2);
        visit4.setPatient(patient);
        visit4.setSpecialization(SpecializationType.SURGEON);
        visitRepo.save(visit4);

        VisitEntity visit5 = new VisitEntity();
        visit5.setVisitDate(visitDate.plusDays(4));
        visit5.setDoctor(doctor2);
        visit5.setPatient(patient);
        visit5.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit5);

        VisitEntity visit6 = new VisitEntity();
        visit6.setVisitDate(visitDate);
        visit6.setDoctor(doctor3);
        visit6.setPatient(patient);
        visit6.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit6);

        // when
        List<DoctorEntity> expected = doctorRepo.findTwoDoctorsWithTheHighestNumberOfVisit();

        // then
        assertThat(expected).hasSize(2);
        assertThat(expected.get(0).getId()).isEqualTo(doctor2.getId());
        assertThat(expected.get(1).getId()).isEqualTo(doctor1.getId());
    }

    @Test
    void shouldDeleteDoctorAndVisitsAndDuties_whenDeletingDoctor_cascade() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        LocalDateTime startDuty1 = LocalDateTime.of(2022, 11, 10, 12, 0);
        LocalDateTime endDuty1 = LocalDateTime.of(2022, 11, 10, 13, 15);

        DutyEntity duty1 = new DutyEntity(doctor1, startDuty1, endDuty1);
        dutyRepo.save(duty1);
        DutyEntity duty2 = new DutyEntity(doctor1, startDuty1.plusDays(1), endDuty1.plusDays(1));
        dutyRepo.save(duty2);
        DutyEntity duty3 = new DutyEntity(doctor2, startDuty1, endDuty1);
        dutyRepo.save(duty3);

        PatientEntity patient1 = patientEntity();
        patient1 = patientRepo.save(patient1);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient1);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate1);
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient1);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate1);
        visit3.setDoctor(doctor2);
        visit3.setPatient(patient1);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visit3 = visitRepo.save(visit3);

        // when
        doctorRepo.deleteById(doctor1.getId());

        // then
        List<PatientEntity> expectedPatients = patientRepo.findAll();
        List<DoctorEntity> expectedDoctors = doctorRepo.findAll();
        List<SpecializationEntity> expectedSpecialization = specializationRepo.findAll();
        List<VisitEntity> expectedVisits = visitRepo.findAll();
        List<DutyEntity> expectedDuties = dutyRepo.findAll();

        assertThat(expectedPatients).hasSize(1);
        assertThat(expectedPatients.get(0).getId()).isEqualTo(patient1.getId());

        assertThat(expectedDoctors).hasSize(1);
        assertThat(expectedDoctors.get(0).getId()).isEqualTo(doctor2.getId());

        assertThat(expectedSpecialization).hasSize(1);
        assertThat(expectedSpecialization.get(0).getId()).isEqualTo(specializationEntity.getId());

        assertThat(expectedVisits).hasSize(1);
        assertThat(expectedVisits.get(0).getId()).isEqualTo(visit3.getId());

        assertThat(expectedDuties).hasSize(1);
        assertThat(expectedDuties.get(0).getId()).isEqualTo(duty3.getId());
    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingDoctor_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);
        doctor.setFirstName("Adam");
        doctorRepo.save(doctor);

        // when && then
        DoctorEntity finalDoctor = doctor;
        assertThatThrownBy(() -> doctorRepo.save(finalDoctor)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnDoctorWithCreatedDateAndUpdated_whenCreatingAndUpdatingDoctor() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.DENTIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));

        // when
        doctor = doctorRepo.save(doctor);
        doctor = doctorRepo.save(doctor);
        LocalDateTime localDateTime = LocalDateTime.now();

        // && then
        assertThat(doctor.getCreateDate()).isAfter(localDateTime.minusSeconds(2));
        assertThat(doctor.getCreateDate()).isBeforeOrEqualTo(doctor.getUpdateDate());
    }

    @Test
    void shouldReturnListOfMatchingDoctors_whenFindAllDoctorsWithAvailableVisitDateBySpecializationAndDate() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        SpecializationEntity specializationEntity2 = specializationEntity(SpecializationType.SURGEON);
        specializationEntity2 = specializationRepo.save(specializationEntity2);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        DoctorEntity doctor3 = doctorEntity();
        doctor3.setSpecializations(List.of(specializationEntity));
        doctor3 = doctorRepo.save(doctor3);

        DoctorEntity doctor4 = doctorEntity();
        doctor4.setSpecializations(List.of(specializationEntity2));
        doctor4 = doctorRepo.save(doctor4);

        DoctorEntity doctor5 = doctorEntity();
        doctor5.setSpecializations(List.of(specializationEntity));
        doctor5 = doctorRepo.save(doctor5);

        LocalDateTime startDuty1 = LocalDateTime.of(2022, 12, 12, 10, 0);
        LocalDateTime endDuty1 = LocalDateTime.of(2022, 12, 12, 10, 30);

        DutyEntity duty1 = new DutyEntity(doctor1, startDuty1, endDuty1);
        dutyRepo.save(duty1);
        DutyEntity duty2 = new DutyEntity(doctor2, startDuty1, endDuty1);
        dutyRepo.save(duty2);
        DutyEntity duty3 = new DutyEntity(doctor3, startDuty1, endDuty1);
        dutyRepo.save(duty3);

        DutyEntity duty4 = new DutyEntity(doctor4, startDuty1, endDuty1);
        dutyRepo.save(duty4);

        DutyEntity duty5 = new DutyEntity(doctor5, startDuty1.minusDays(1), endDuty1.minusDays(1));
        dutyRepo.save(duty5);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

        PatientEntity patient3 = patientEntity();
        patient3 = patientRepo.save(patient3);

        LocalDateTime visitDate = LocalDateTime.of(2022, 12, 12, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.plusMinutes(15));
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient2);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate);
        visit3.setDoctor(doctor2);
        visit3.setPatient(patient3);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit3);

        LocalDate dateVisit = LocalDate.of(2022, 12, 12);

        // when
        List<DoctorEntity> expected = doctorRepo.findAllDoctorsWithAvailableVisitDateBySpecializationAndDate(dateVisit, SpecializationType.OCULIST);

        // then
        assertThat(expected).hasSize(2);
    }

    @Test
    void shouldReturnBuisnessException_whenFindAllDoctorsWithAvailableVisitDateBySpecializationAndDate_andNoDoctorsAvailable() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        LocalDateTime startDuty1 = LocalDateTime.of(2022, 12, 12, 10, 0);
        LocalDateTime endDuty1 = LocalDateTime.of(2022, 12, 12, 10, 30);

        DutyEntity duty4 = new DutyEntity(doctor1, startDuty1, endDuty1);
        dutyRepo.save(duty4);

        DutyEntity duty5 = new DutyEntity(doctor2, startDuty1.minusDays(1), endDuty1.minusDays(1));
        dutyRepo.save(duty5);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

        LocalDateTime visitDate = LocalDateTime.of(2022, 12, 12, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.plusMinutes(15));
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient2);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        LocalDate dateVisit = LocalDate.of(2022, 12, 12);

        // when && then
        assertThatThrownBy(() ->doctorRepo.findAllDoctorsWithAvailableVisitDateBySpecializationAndDate(dateVisit, SpecializationType.OCULIST))
                .isInstanceOf(BusinessException.class);
    }
}