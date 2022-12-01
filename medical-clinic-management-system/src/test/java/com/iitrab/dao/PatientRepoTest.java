package com.iitrab.dao;

import com.iitrab.AbstractTest;
import com.iitrab.domain.*;
import com.iitrab.exception.api.BusinessException;
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
class PatientRepoTest extends AbstractTest {

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private SpecializationRepo specializationRepo;


    @Test
    void shouldReturnPatientWithTheHighestNumberOfVisitWithStatusUnrealized_whenFindingPatientWithTheHighestNumberOfVisitWithStatusUnrealized() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        PatientEntity patient1 = patientEntity();
        patient1 = patientRepo.save(patient1);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

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
        visit3.setDoctor(doctor1);
        visit3.setPatient(patient2);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate1.plusDays(3));
        visit4.setDoctor(doctor1);
        visit4.setPatient(patient2);
        visit4.setSpecialization(SpecializationType.OCULIST);
        visit4 = visitRepo.save(visit4);
        visit4.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit4);

        // when
        PatientEntity expected = patientRepo.findPatientWithTheHighestNumberOfVisitWithStatusUnrealized(visitDate1.minusDays(1), visitDate1.plusDays(1));

        // then
        assertThat(expected).isNotNull();
        assertThat(expected.getId()).isEqualTo(patient1.getId());
    }

    @Test
    void shouldReturnBusinessException_whenFindingPatientWithTheHighestNumberOfVisitWithStatusUnrealized_andNoPatientWithVisitStatusUnrealized() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        PatientEntity patient1 = patientEntity();
        patient1 = patientRepo.save(patient1);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

        LocalDateTime visitDate1 = LocalDateTime.of(2022, 11, 22, 10, 0);

        VisitEntity visit1 = new VisitEntity();
        visit1.setVisitDate(visitDate1);
        visit1.setDoctor(doctor1);
        visit1.setPatient(patient1);
        visit1.setSpecialization(SpecializationType.OCULIST);
        visit1 = visitRepo.save(visit1);
        visit1.setVisitStatus(VisitStatus.PENDING);
        visitRepo.save(visit1);

        VisitEntity visit2 = new VisitEntity();
        visit2.setVisitDate(visitDate1);
        visit2.setDoctor(doctor1);
        visit2.setPatient(patient1);
        visit2.setSpecialization(SpecializationType.OCULIST);
        visit2 = visitRepo.save(visit2);
        visit2.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit2);

        VisitEntity visit3 = new VisitEntity();
        visit3.setVisitDate(visitDate1);
        visit3.setDoctor(doctor1);
        visit3.setPatient(patient2);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.FINISHED);
        visitRepo.save(visit3);

        VisitEntity visit4 = new VisitEntity();
        visit4.setVisitDate(visitDate1.plusDays(3));
        visit4.setDoctor(doctor1);
        visit4.setPatient(patient2);
        visit4.setSpecialization(SpecializationType.OCULIST);
        visit4 = visitRepo.save(visit4);
        visit4.setVisitStatus(VisitStatus.PENDING);
        visitRepo.save(visit4);

        // when && then
        assertThatThrownBy(() -> patientRepo.findPatientWithTheHighestNumberOfVisitWithStatusUnrealized(visitDate1.minusDays(1), visitDate1.plusDays(1)))
                .isInstanceOf(BusinessException.class);
    }
    
    @Test
    void shouldDeletePatientAndVisits_whenDeletingPatient_cascade() {
        // given
        SpecializationEntity specializationEntity = specializationEntity(SpecializationType.OCULIST);
        specializationEntity = specializationRepo.save(specializationEntity);

        DoctorEntity doctor1 = doctorEntity();
        doctor1.setSpecializations(List.of(specializationEntity));
        doctor1 = doctorRepo.save(doctor1);

        PatientEntity patient1 = patientEntity();
        patient1 = patientRepo.save(patient1);

        PatientEntity patient2 = patientEntity();
        patient2 = patientRepo.save(patient2);

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
        visit3.setDoctor(doctor1);
        visit3.setPatient(patient2);
        visit3.setSpecialization(SpecializationType.OCULIST);
        visit3 = visitRepo.save(visit3);
        visit3.setVisitStatus(VisitStatus.UNREALIZED_PATIENT);
        visit3 = visitRepo.save(visit3);

        // when
        patientRepo.deleteById(patient1.getId());
        
        // then
        List<PatientEntity> expectedPatients = patientRepo.findAll();
        List<DoctorEntity> expectedDoctors = doctorRepo.findAll();
        List<SpecializationEntity> expectedSpecialization = specializationRepo.findAll();
        List<VisitEntity> expectedVisits = visitRepo.findAll();
        
        assertThat(expectedPatients).hasSize(1);
        assertThat(expectedPatients.get(0).getId()).isEqualTo(patient2.getId());

        assertThat(expectedDoctors).hasSize(1);
        assertThat(expectedDoctors.get(0).getId()).isEqualTo(doctor1.getId());

        assertThat(expectedSpecialization).hasSize(1);
        assertThat(expectedSpecialization.get(0).getId()).isEqualTo(specializationEntity.getId());

        assertThat(expectedVisits).hasSize(1);
        assertThat(expectedVisits.get(0).getId()).isEqualTo(visit3.getId());
    }

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingPatient_withNoRefreshEntityVersion() {
        // given
        PatientEntity patient = patientEntity();
        patient = patientRepo.save(patient);
        patient.setLastName("aa");
        patientRepo.save(patient);

        // when && then
        PatientEntity finalPatient = patient;
        assertThatThrownBy(() -> patientRepo.save(finalPatient)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnPatientWithCreatedDateAndUpdated_whenCreatingAndUpdating() {
        // given
        PatientEntity patient = patientEntity();

        // when
        patient = patientRepo.save(patient);
        patient = patientRepo.save(patient);
        LocalDateTime localDateTime = LocalDateTime.now();

        // && then
        assertThat(patient.getCreateDate()).isAfter(localDateTime.minusSeconds(2));
        assertThat(patient.getCreateDate()).isBefore(localDateTime);
        assertThat(patient.getCreateDate()).isBefore(patient.getUpdateDate());
    }
}