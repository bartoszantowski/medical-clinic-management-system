package com.iitrab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PATIENT")
@Getter
@Setter
@NamedQuery(name = PatientEntity.QUERY_FIND_PATIENT_WITH_THE_HIGHEST_NUMBER_OF_VISIT_WITH_STATUS_UNREALIZED,
        query = "SELECT p FROM PatientEntity p JOIN p.visits v WHERE v.visitStatus = 'UNREALIZED_PATIENT' OR v.visitStatus = 'UNREALIZED_OTHERS' AND v.visitDate BETWEEN :dateFrom AND :dateTo GROUP BY p.id, v.visitStatus ORDER BY count(v.id) DESC")
public class PatientEntity extends AbstractPerson {
    public static final String QUERY_FIND_PATIENT_WITH_THE_HIGHEST_NUMBER_OF_VISIT_WITH_STATUS_UNREALIZED = "Patient.findPatientWithTheHighestNumberOfVisitWithStatusUnrealized";

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE})
    private List<VisitEntity> visits;
}
