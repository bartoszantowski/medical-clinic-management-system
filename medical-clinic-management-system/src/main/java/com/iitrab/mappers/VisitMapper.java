package com.iitrab.mappers;

import com.iitrab.domain.VisitEntity;
import com.iitrab.types.BaseSpecializationTO;
import com.iitrab.types.VisitTO;

public class VisitMapper {
    public static VisitEntity toVisitEntity(BaseSpecializationTO customVisitSpecializationTO) {
        if (customVisitSpecializationTO == null) {
            return null;
        }

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setVisitDate(customVisitSpecializationTO.getVisitDate());
        visitEntity.setSpecialization(customVisitSpecializationTO.getDoctorSpecializationType());

        return visitEntity;
    }

    public static VisitTO toVisitTO(VisitEntity visitEntity) {
        if (visitEntity == null) {
            return null;
        }

        return VisitTO.builder()
                .patientId(visitEntity.getPatient().getId())
                .visitDate(visitEntity.getVisitDate())
                .doctorFirstName(visitEntity.getDoctor().getFirstName())
                .doctorLastName(visitEntity.getDoctor().getLastName())
                .specializationType(visitEntity.getSpecialization())
                .build();
    }
}
