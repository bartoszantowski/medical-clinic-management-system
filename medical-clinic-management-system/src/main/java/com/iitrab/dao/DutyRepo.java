package com.iitrab.dao;

import com.iitrab.domain.DutyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DutyRepo extends JpaRepository<DutyEntity, Long>, DutyRepoCustom {
}
