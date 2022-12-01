package com.iitrab.types;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DutyTO {
    private String doctorFirstName;
    private String doctorLastName;
    private LocalDateTime startDutyDate;
    private LocalDateTime endDutyDate;
}
