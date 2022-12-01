package com.iitrab.dao.impl;

import com.iitrab.domain.SpecializationType;
import com.iitrab.domain.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitSearchCriteria {
    private VisitStatus visitStatus;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private SpecializationType specializationType;
    private String patientFirstName;
    private String patientLastName;
}
