package com.iitrab.types;

import com.iitrab.domain.SpecializationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomVisitDoctorAndSpecializationTO extends BaseSpecializationTO {

    private Long patientId;
    private Long doctorId;

    public CustomVisitDoctorAndSpecializationTO(Long patientId, Long doctorId, SpecializationType doctorSpecializationType, LocalDateTime visitDate) {
        super(doctorSpecializationType, visitDate);
        this.patientId = patientId;
        this.doctorId = doctorId;
    }
}
