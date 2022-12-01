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
public class CustomVisitSpecializationTO extends BaseSpecializationTO {

    private Long patientId;

    public CustomVisitSpecializationTO(Long patientId, SpecializationType doctorSpecializationType, LocalDateTime visitDate) {
        super(doctorSpecializationType, visitDate);
        this.patientId = patientId;
    }
}
