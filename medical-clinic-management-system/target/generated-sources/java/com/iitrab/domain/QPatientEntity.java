package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPatientEntity is a Querydsl query type for PatientEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPatientEntity extends EntityPathBase<PatientEntity> {

    private static final long serialVersionUID = -729231648L;

    public static final QPatientEntity patientEntity = new QPatientEntity("patientEntity");

    public final QAbstractPerson _super = new QAbstractPerson(this);

    //inherited
    public final StringPath address = _super.address;

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final StringPath firstName = _super.firstName;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath lastName = _super.lastName;

    public final EnumPath<Sex> sex = createEnum("sex", Sex.class);

    //inherited
    public final StringPath telephoneNumber = _super.telephoneNumber;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final ListPath<VisitEntity, QVisitEntity> visits = this.<VisitEntity, QVisitEntity>createList("visits", VisitEntity.class, QVisitEntity.class, PathInits.DIRECT2);

    public QPatientEntity(String variable) {
        super(PatientEntity.class, forVariable(variable));
    }

    public QPatientEntity(Path<? extends PatientEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPatientEntity(PathMetadata metadata) {
        super(PatientEntity.class, metadata);
    }

}

