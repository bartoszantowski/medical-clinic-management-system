package com.iitrab.dao.impl;

import com.iitrab.AbstractTest;
import com.iitrab.dao.*;
import com.iitrab.domain.*;
import com.iitrab.exception.api.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.iitrab.SampleTestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VisitRepoCustomImplTest extends AbstractTest {

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
    void shouldReturnAllVisits_whenFindAllVisitByCriteria_andVisitSearchCriteriaIsEmpty() {
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

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);
        LocalDateTime visitDate2 = LocalDateTime.of(2022, 11, 22, 10, 30);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate2);
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        VisitSearchCriteria visitSearchCriteria = new VisitSearchCriteria();

        // when
        List<VisitEntity> expected = visitRepo.findAllVisitsByCriteria(visitSearchCriteria);

        // then
        assertThat(expected).hasSize(2);
    }

    @Test
    void shouldReturnAllVisits_whenFindAllVisitByCriteria() {
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

        PatientEntity patient1 = patientEntity();
        patient1.setFirstName("TEST");
        patient1.setLastName(patient.getLastName());
        patientRepo.save(patient1);

        PatientEntity patient2 = patientEntity();
        patient2.setFirstName(patient.getFirstName());
        patient2.setLastName("TEST");
        patientRepo.save(patient2);

        LocalDateTime visitDate = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity expected = new VisitEntity();
        expected.setVisitDate(visitDate);
        expected.setDoctor(doctor);
        expected.setPatient(patient);
        expected.setSpecialization(SpecializationType.OCULIST);
        expected = visitRepo.save(expected);
        
        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate.minusHours(2));
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate.plusHours(3));
        visit3.setDoctor(doctor);
        visit3.setPatient(patient);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate);
        visit4.setDoctor(doctor);
        visit4.setPatient(patient);
        visit4.setSpecialization(SpecializationType.SURGEON);
        visitRepo.save(visit4);

        VisitEntity visit5 = new VisitEntity();
        visit5.setVisitDate(visitDate);
        visit5.setDoctor(doctor);
        visit5.setPatient(patient1);
        visit5.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit5);

        VisitEntity visit6 = new VisitEntity();
        visit6.setVisitDate(visitDate);
        visit6.setDoctor(doctor);
        visit6.setPatient(patient2);
        visit6.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit6);

        VisitSearchCriteria visitSearchCriteria = new VisitSearchCriteria(
                expected.getVisitStatus(),
                expected.getVisitDate().minusHours(1),
                expected.getVisitDate().plusHours(1),
                expected.getSpecialization(),
                patient.getFirstName(),
                patient.getLastName()
        );

        // when
        List<VisitEntity> expectedVisits = visitRepo.findAllVisitsByCriteria(visitSearchCriteria);

        // then
        assertThat(expectedVisits).hasSize(1);
    }

    @Test
    void shouldReturnTotalVisitsCost_whenCalculatingTotalVisitCostByPatientAndDate() {
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setHourlyRate(new BigDecimal("240.00"));
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        DoctorEntity doctor2 = doctorEntity();
        doctor2.setHourlyRate(new BigDecimal("400.00"));
        doctor2.setSpecializations(List.of(specializationEntity));
        doctor2 = doctorRepo.save(doctor2);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);
        LocalDateTime visitDate2 = LocalDateTime.of(2022, 11, 22, 10, 30);
        LocalDateTime visitDate3 = LocalDateTime.of(2022, 11, 23, 11, 30);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate2);
        visit2.setDoctor(doctor2);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate3);
        visit3.setDoctor(doctor2);
        visit3.setPatient(patient);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate2.plusMinutes(15));
        visit4.setDoctor(doctor2);
        visit4.setPatient(patient);
        visit4.setSpecialization(SpecializationType.OCULIST);
        visit4 = visitRepo.save(visit4);
        visit4.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit4);

        // when
        BigDecimal expected = visitRepo.calculateTotalVisitCostByPatientAndDate(patient.getId(), visitDate1.minusHours(1), visitDate2.plusHours(2));

        // then
        assertThat(expected.toBigInteger()).isEqualTo((260));
    }

    @Test
    void shouldReturnIllegalArgumentException_whenCalculatingTotalVisitCostByPatientAndDate_andPatientIdIsNull() {
        // given
        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        // when && then
        assertThatThrownBy(() ->
                visitRepo.calculateTotalVisitCostByPatientAndDate(null, visitDate1, visitDate1.plusHours(2)))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    void shouldReturnIllegalArgumentException_whenCalculatingTotalVisitCostByPatientAndDate_andStartDateIsNull() {
        // given
        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        // when && then
        assertThatThrownBy(() ->
                visitRepo.calculateTotalVisitCostByPatientAndDate(55L, null, visitDate1.plusHours(2)))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    void shouldReturnIllegalArgumentException_whenCalculatingTotalVisitCostByPatientAndDate_andEndDateIsNull() {
        // given
        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        // when && then
        assertThatThrownBy(() ->
                visitRepo.calculateTotalVisitCostByPatientAndDate(55L, visitDate1, null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    void shouldReturnSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized_whenFindingSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);
        
        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate1);
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.UNREALIZED_OTHERS);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate1);
        visit3.setDoctor(doctor);
        visit3.setPatient(patient);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate1);
        visit4.setDoctor(doctor);
        visit4.setPatient(patient);
        visit4.setSpecialization(SpecializationType.DENTIST);
        visitRepo.save(visit4);

        // when
        SpecializationType expected = visitRepo.findSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized();

        // then
        assertThat(expected).isNotNull();
        assertThat(expected).isEqualTo(SpecializationType.OCULIST);
    }

    @Test
    void shouldReturnBusinessException_whenFindingSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized_andNoVisitWithStatusUnrealized() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate1);
        visit2.setDoctor(doctor);
        visit2.setPatient(patient);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visitRepo.save(visit2);

        // when && then
        assertThatThrownBy(() ->
                visitRepo.findSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized())
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingSpecialization_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit1);

        // when && then
        VisitEntity finalVisit = visit1;
        assertThatThrownBy(() -> visitRepo.save(finalVisit)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnSpecializationWithCreatedDateAndUpdated_whenCreatingAndUpdating() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor = doctorEntity();
        doctor.setSpecializations(List.of(specializationEntity));
        doctor = doctorRepo.save(doctor);

        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor);
        visit1.setPatient(patient);
        visit1.setSpecialization(SpecializationType.DENTIST);

        // when
        visit1 = visitRepo.save(visit1);
        LocalDateTime localDateTime = LocalDateTime.now();
        visit1 = visitRepo.save(visit1);

        // && then
        assertThat(visit1.getCreateDate()).isAfter(localDateTime.minusSeconds(2));
        assertThat(visit1.getCreateDate()).isBefore(visit1.getUpdateDate());
    }
}