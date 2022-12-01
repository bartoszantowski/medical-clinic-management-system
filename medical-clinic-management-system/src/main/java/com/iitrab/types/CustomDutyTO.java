package com.iitrab.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomDutyTO {
    private Long doctorId;
    private LocalDateTime startDutyDate;
    private LocalDateTime endDutyDate;
}
