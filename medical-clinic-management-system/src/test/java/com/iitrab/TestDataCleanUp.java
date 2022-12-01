package com.iitrab;

import com.iitrab.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataCleanUp {

    @Autowired
    VisitRepo visitRepo;

    @Autowired
    SpecializationRepo specializationRepo;

    @Autowired
    DutyRepo dutyRepo;

    @Autowired
    DoctorRepo doctorRepo;

    @Autowired
    PatientRepo patientRepo;


    public void cleanUp() {
        visitRepo.deleteAll();
        doctorRepo.deleteAll();
        specializationRepo.deleteAll();
        dutyRepo.deleteAll();
        patientRepo.deleteAll();
    }
}
