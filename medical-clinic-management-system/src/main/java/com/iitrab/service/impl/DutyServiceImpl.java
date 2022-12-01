package com.iitrab.service.impl;

import com.iitrab.dao.DoctorRepo;
import com.iitrab.dao.DutyRepo;
import com.iitrab.domain.DoctorEntity;
import com.iitrab.domain.DutyEntity;
import com.iitrab.mappers.DutyMapper;
import com.iitrab.service.DutyService;
import com.iitrab.types.CustomDutyTO;
import com.iitrab.types.DutyTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DutyServiceImpl implements DutyService {

    @Autowired
    private DutyRepo dutyRepo;

    @Autowired
    private DutyValidator dutyValidator;

    @Autowired
    private DoctorRepo doctorRepo;


    @Override
    public DutyTO addDoctorsDuty(CustomDutyTO customDutyTO) {
        dutyValidator.ifDutyCanBeAdded(customDutyTO);

        DoctorEntity doctorEntity = doctorRepo.getOne(customDutyTO.getDoctorId());
        DutyEntity dutyEntity = DutyMapper.toDutyEntity(customDutyTO);
        dutyEntity.setDoctor(doctorEntity);
        dutyEntity = dutyRepo.save(dutyEntity);

        return DutyMapper.toDutyTO(dutyEntity, doctorEntity);
    }
}
