package com.iitrab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SPECIALIZATION")
@Getter
@Setter
public class SpecializationEntity extends AbstractEntity{

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private SpecializationType specializationType;

    @ManyToMany(mappedBy = "specializations")
    List<DoctorEntity> doctors;
}
