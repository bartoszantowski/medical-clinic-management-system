package com.iitrab.dao;

import com.iitrab.AbstractTest;
import com.iitrab.domain.SpecializationEntity;
import com.iitrab.domain.SpecializationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class SpecializationRepoTest extends AbstractTest {

    @Autowired
    private SpecializationRepo specializationRepo;

    @Test
    void shouldReturnObjectOptimisticLockingFailureException_whenUpdatingSpecialization_withNoRefreshEntityVersion() {
        // given
        SpecializationEntity specialization = new SpecializationEntity();
        specialization.setSpecializationType(SpecializationType.OCULIST);
        specialization = specializationRepo.save(specialization);
        specializationRepo.save(specialization);

        // when && then
        SpecializationEntity finalSpecialization = specialization;
        assertThatThrownBy(() -> specializationRepo.save(finalSpecialization)).isInstanceOf(
                ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnSpecializationWithCreatedDateAndUpdated_whenCreatingAndUpdating() {
        // given
        SpecializationEntity specialization = new SpecializationEntity();
        specialization.setSpecializationType(SpecializationType.OCULIST);

        // when
        specialization = specializationRepo.save(specialization);
        specialization = specializationRepo.save(specialization);

        LocalDateTime localDateTime = LocalDateTime.now();

        // && then
        assertThat(specialization.getCreateDate()).isAfter(localDateTime.minusSeconds(2));
        assertThat(specialization.getCreateDate()).isBefore(localDateTime);
        assertThat(specialization.getCreateDate()).isBefore(specialization.getUpdateDate());
    }
}