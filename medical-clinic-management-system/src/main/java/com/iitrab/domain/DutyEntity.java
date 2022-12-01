package com.iitrab.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DUTY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DutyEntity extends AbstractEntity {

    @JoinColumn(name = "DOCTOR_ID", nullable = false)
    @ManyToOne
    private DoctorEntity doctor;

    @Column(nullable = false)
    private LocalDateTime startDutyDate;

    @Column(nullable = false)
    private LocalDateTime endDutyDate;
}
