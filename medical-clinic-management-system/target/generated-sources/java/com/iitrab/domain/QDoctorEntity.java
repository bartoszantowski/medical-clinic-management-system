package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDoctorEntity is a Querydsl query type for DoctorEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDoctorEntity extends EntityPathBase<DoctorEntity> {

    private static final long serialVersionUID = 506711882L;

    public static final QDoctorEntity doctorEntity = new QDoctorEntity("doctorEntity");

    public final QAbstractPerson _super = new QAbstractPerson(this);

    //inherited
    public final StringPath address = _super.address;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<DutyEntity, QDutyEntity> duties = this.<DutyEntity, QDutyEntity>createList("duties", DutyEntity.class, QDutyEntity.class, PathInits.DIRECT2);

    //inherited
    public final StringPath firstName = _super.firstName;

    public final NumberPath<java.math.BigDecimal> hourlyRate = createNumber("hourlyRate", java.math.BigDecimal.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath lastName = _super.lastName;

    public final ListPath<SpecializationEntity, QSpecializationEntity> specializations = this.<SpecializationEntity, QSpecializationEntity>createList("specializations", SpecializationEntity.class, QSpecializationEntity.class, PathInits.DIRECT2);

    //inherited
    public final StringPath telephoneNumber = _super.telephoneNumber;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final ListPath<VisitEntity, QVisitEntity> visits = this.<VisitEntity, QVisitEntity>createList("visits", VisitEntity.class, QVisitEntity.class, PathInits.DIRECT2);

    public QDoctorEntity(String variable) {
        super(DoctorEntity.class, forVariable(variable));
    }

    public QDoctorEntity(Path<? extends DoctorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDoctorEntity(PathMetadata metadata) {
        super(DoctorEntity.class, metadata);
    }

}

