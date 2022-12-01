package com.iitrab.dao;

import com.iitrab.dao.impl.VisitSearchCriteria;
import com.iitrab.domain.SpecializationType;
import com.iitrab.domain.VisitEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepoCustom {

    /**
     * Query finds all visits according to given criteria.
     * Each of the criteria is optional.
     * Available criteria {@link VisitSearchCriteria }
     *
     * @param visitSearchCriteria criteria form object
     * @return list of matching visits
     */
    List<VisitEntity> findAllVisitsByCriteria(VisitSearchCriteria visitSearchCriteria);

    /**
     * Query finds all visits of a given patient to a given specialist in the last month
     * counting from the moment of invoking the query
     * (significant only date - not time).
     *
     * @param specializationType doctor specialization type
     * @param patientId patient Id
     * @return list of visits
     */
    List<VisitEntity> findAllVisitsFromLastMonthWithStatusUnrealizedPatientByPatientId(SpecializationType specializationType, Long patientId);

    /**
     * Query calculates the total cost of visits for the given patient in the given time period.
     * Query includes all visits appearing in the system, regardless of their status
     *
     * @param patientId patient Id
     * @param startDate date from
     * @param endDate date to
     * @return total cost of visits
     */
    BigDecimal calculateTotalVisitCostByPatientAndDate(@NotNull Long patientId,
                                                       @NotNull LocalDateTime startDate,
                                                       @NotNull LocalDateTime endDate);

    /**
     * Query finds the name of the specialization that has the most visits with status unrealized
     * (UNREALIZED_PATIENT and UNREALIZED_OTHERS)
     *
     * @return specialization type or exception
     */
    SpecializationType findSpecializationTypeWithTheHighestNumberOfVisitsWithStatusUnrealized();
}
