package com.iitrab.types;

import com.iitrab.domain.SpecializationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseSpecializationTO {
    private SpecializationType doctorSpecializationType;
    private LocalDateTime visitDate;
}
