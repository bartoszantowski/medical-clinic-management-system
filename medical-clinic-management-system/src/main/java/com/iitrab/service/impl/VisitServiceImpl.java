package com.iitrab.service.impl;

import com.iitrab.dao.DoctorRepo;
import com.iitrab.dao.PatientRepo;
import com.iitrab.dao.VisitRepo;
import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.PatientEntity;
import com.iitrab.domain.VisitEntity;
import com.iitrab.mappers.VisitMapper;
import com.iitrab.service.VisitService;
import com.iitrab.types.CustomVisitDoctorAndSpecializationTO;
import com.iitrab.types.CustomVisitSpecializationTO;
import com.iitrab.types.VisitTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private VisitValidator visitValidator;

    @Override
    public VisitTO scheduleVisitBySpecialization(CustomVisitSpecializationTO customVisitSpecializationTO) {
        visitValidator.ifVisitBySpecializationCanBeAdded(customVisitSpecializationTO);

        DoctorEntity availableDoctor = doctorRepo.findAllDoctorsBySpecializationAndAvailableThatDate(customVisitSpecializationTO);
        PatientEntity patient = patientRepo.getOne(customVisitSpecializationTO.getPatientId());

        VisitEntity visitEntity = VisitMapper.toVisitEntity(customVisitSpecializationTO);
        visitEntity.setDoctor(availableDoctor);
        visitEntity.setPatient(patient);
        visitRepo.save(visitEntity);

        return VisitMapper.toVisitTO(visitEntity);
    }

    public VisitTO scheduleVisitByDoctorAndSpecialization(CustomVisitDoctorAndSpecializationTO custom) {
        visitValidator.ifVisitByDoctorCanBeAdded(custom);

        DoctorEntity doctor = doctorRepo.getOne(custom.getDoctorId());
        PatientEntity patient = patientRepo.getOne(custom.getPatientId());

        VisitEntity visitEntity = VisitMapper.toVisitEntity(custom);
        visitEntity.setDoctor(doctor);
        visitEntity.setPatient(patient);
        visitRepo.save(visitEntity);

        return VisitMapper.toVisitTO(visitEntity);
    }
}
