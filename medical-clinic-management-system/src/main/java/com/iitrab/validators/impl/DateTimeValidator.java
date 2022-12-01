package com.iitrab.validators.impl;

import com.iitrab.exception.api.DateTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Class for date and time validation.
 * Checks the compliance of the given dates with applicable regulations.
 */
@Component
@Slf4j
class DateTimeValidator {
    private static final LocalTime START_TIME_OF_WORK = LocalTime.of(8, 0);
    private static final LocalTime END_TIME_OF_WORK = LocalTime.of(18, 0);
    private static final String THIS_DATE_IS_NOT_CORRECT_ALLOWED_TIME_HH_00_15_30_45 = " - this date is not correct. Allowed time -> HH: 00/15/30/45";
    private static final String ISN_T_WORKING_TIME_8_18 = " isn't working time (8 - 18)!";
    private static final String ISN_T_WORKING_DAY_MON_FRI = " isn't working day (MON - FRI)!";
    private static final String START_DATE_CAN_T_BE_AFTER_END_DATE = "Start date can't be after end date!!!";
    private final List<Integer> allowedMinutes = List.of(0, 15, 30, 45);


    public void validDutyDateTime(LocalDateTime startDutyDate, LocalDateTime endDutyDate) {
        valid(startDutyDate, endDutyDate);
    }

    private void valid(LocalDateTime startDutyDate, LocalDateTime endDutyDate) {
        ifStartDutyIsNotAfterEndDuty(startDutyDate, endDutyDate);
        ifDatesIsInCorrectDaysAndTimes(startDutyDate);
        ifDatesIsInCorrectDaysAndTimes(endDutyDate);
    }

    public void validVisitDateTime(LocalDateTime visitDate) {
        ifDatesIsInCorrectDaysAndTimes(visitDate);
    }

    /**
     * Working day is MON to FRI.
     * Working time is 8:00am to 6:00pm
     * Correct interval is 15'
     *
     * @param dateTime
     */
    private void ifDatesIsInCorrectDaysAndTimes(LocalDateTime dateTime) {
        ifIsWorkingDay(dateTime);
        ifIsWorkingTime(dateTime);
        ifIsCorrectIntervalTime(dateTime);
    }

    private void ifIsCorrectIntervalTime(LocalDateTime date) {
        if (!allowedMinutes.contains(date.getMinute())) {
            throw new DateTimeException(date.toLocalTime() + THIS_DATE_IS_NOT_CORRECT_ALLOWED_TIME_HH_00_15_30_45);
        }
    }

    private void ifIsWorkingTime(LocalDateTime date) {
        if (date.toLocalTime().isBefore(START_TIME_OF_WORK) || date.toLocalTime().isAfter(END_TIME_OF_WORK)) {
            throw new DateTimeException(date.toLocalTime() + ISN_T_WORKING_TIME_8_18);
        }
    }

    /**
     * Monday is 1
     * Sunday is 7
     *
     * @param date
     */
    private void ifIsWorkingDay(LocalDateTime date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        if (isNotWorkingDay(dayOfWeek)) {
            throw new DateTimeException(date.getDayOfWeek() + ISN_T_WORKING_DAY_MON_FRI);
        }
    }

    private boolean isNotWorkingDay(int dayOfWeek) {
        return dayOfWeek > 5;
    }

    private void ifStartDutyIsNotAfterEndDuty(LocalDateTime startDutyDate, LocalDateTime endDutyDate) {
        if (startDutyDate.isAfter(endDutyDate)) {
            throw new DateTimeException(START_DATE_CAN_T_BE_AFTER_END_DATE);
        }
    }
}
