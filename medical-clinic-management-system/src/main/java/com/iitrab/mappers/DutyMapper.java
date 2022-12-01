package com.iitrab.mappers;

import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.DutyEntity;
import com.iitrab.types.CustomDutyTO;
import com.iitrab.types.DutyTO;

public class DutyMapper {

    public static DutyEntity toDutyEntity(CustomDutyTO customDutyTO) {
        if (customDutyTO == null) {
            return null;
        }
        DutyEntity dutyEntity = new DutyEntity();
        dutyEntity.setStartDutyDate(customDutyTO.getStartDutyDate());
        dutyEntity.setEndDutyDate(customDutyTO.getEndDutyDate());

        return dutyEntity;
    }

    public static DutyTO toDutyTO(DutyEntity dutyEntity, DoctorEntity doctorEntity) {
        if (dutyEntity == null || doctorEntity == null) {
            return null;
        }

        return DutyTO.builder()
                .doctorFirstName(doctorEntity.getFirstName())
                .doctorLastName(doctorEntity.getLastName())
                .startDutyDate(dutyEntity.getStartDutyDate())
                .endDutyDate(dutyEntity.getEndDutyDate())
                .build();
    }
}
