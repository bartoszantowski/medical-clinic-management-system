package com.iitrab.dao;

import com.iitrab.domain.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<PatientEntity, Long>, PatientRepoCustom {

}
