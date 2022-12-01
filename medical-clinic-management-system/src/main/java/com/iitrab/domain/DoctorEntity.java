package com.iitrab.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DOCTOR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = DoctorEntity.QUERY_FIND_DOCTOR_WITH_THE_FEWEST_NUMBER_OF_PATIENTS_BETWEEN_DATES,
        query = "SELECT d FROM DoctorEntity d JOIN d.visits v WHERE v.visitDate BETWEEN :dateFrom AND :dateTo GROUP BY v.doctor, v.patient ORDER BY count(DISTINCT v.patient) ASC")
@NamedQuery(name = DoctorEntity.QUERY_FIND_TWO_DOCTORS_WITH_THE_HIGHEST_NUMBER_OF_VISIT,
        query = "SELECT d FROM DoctorEntity d JOIN d.visits v GROUP BY v.doctor ORDER BY count(v.id) DESC")
public class DoctorEntity extends AbstractPerson {
    public static final String QUERY_FIND_DOCTOR_WITH_THE_FEWEST_NUMBER_OF_PATIENTS_BETWEEN_DATES = "Doctor.findDoctorWithTheFewestNumberOfPatientsBetweenDates";
    public static final String QUERY_FIND_TWO_DOCTORS_WITH_THE_HIGHEST_NUMBER_OF_VISIT = "Doctor.findTwoDoctorsWithTheHighestNumberOfVisit";

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @NotNull
    @NotEmpty
    @ManyToMany
    @JoinTable(name = "DOCTOR_TO_SPECIALIZATION",
            joinColumns = {@JoinColumn(name = "SPECIALIZATION_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "DOCTOR_ID", nullable = false, updatable = false)})
    private List<SpecializationEntity> specializations;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.REMOVE})
    private List<VisitEntity> visits;

    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<DutyEntity> duties;
}
