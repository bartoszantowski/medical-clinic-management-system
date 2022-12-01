package com.iitrab.types;

import com.iitrab.domain.SpecializationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VisitTO {
    private Long patientId;
    private LocalDateTime visitDate;
    private String doctorFirstName;
    private String doctorLastName;
    private SpecializationType specializationType;
}
