package com.iitrab.dao;

import com.iitrab.domain.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepo extends JpaRepository<SpecializationEntity, Long>, SpecializationRepoCustom {
}
