package com.iitrab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VISIT")
@Getter
@Setter
@NamedQuery(name = VisitEntity.QUERY_CALCULATE_TOTAL_VISIT_COST_BY_PATIENT_AND_DATE,
        query = "SELECT SUM(d.hourlyRate)/4 FROM VisitEntity v JOIN v.doctor d JOIN v.patient p WHERE p.id = :patientId AND v.visitDate BETWEEN :startDate AND :endDate")
public class VisitEntity extends AbstractEntity {
    public static final String QUERY_CALCULATE_TOTAL_VISIT_COST_BY_PATIENT_AND_DATE = "Visit.calculateTotalVisitCostByPatientAndDate";

    @Column(nullable = false)
    private LocalDateTime visitDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitStatus visitStatus;

    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    @ManyToOne
    private DoctorEntity doctor;

    @JoinColumn(name = "PATIENT_ID", nullable = false)
    @ManyToOne
    private PatientEntity patient;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SpecializationType specialization;

    @PrePersist
    private void setDefaultVisitStatus() {
        this.visitStatus = VisitStatus.PENDING;
    }
}
