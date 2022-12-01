package com.iitrab.dao;

import com.iitrab.domain.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepo extends JpaRepository<VisitEntity, Long>, VisitRepoCustom {
}
