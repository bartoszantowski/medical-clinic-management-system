package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVisitEntity is a Querydsl query type for VisitEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QVisitEntity extends EntityPathBase<VisitEntity> {

    private static final long serialVersionUID = -544347482L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVisitEntity visitEntity = new QVisitEntity("visitEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QDoctorEntity doctor;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QPatientEntity patient;

    public final EnumPath<SpecializationType> specialization = createEnum("specialization", SpecializationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public final DateTimePath<java.time.LocalDateTime> visitDate = createDateTime("visitDate", java.time.LocalDateTime.class);

    public final EnumPath<VisitStatus> visitStatus = createEnum("visitStatus", VisitStatus.class);

    public QVisitEntity(String variable) {
        this(VisitEntity.class, forVariable(variable), INITS);
    }

    public QVisitEntity(Path<? extends VisitEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVisitEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVisitEntity(PathMetadata metadata, PathInits inits) {
        this(VisitEntity.class, metadata, inits);
    }

    public QVisitEntity(Class<? extends VisitEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.doctor = inits.isInitialized("doctor") ? new QDoctorEntity(forProperty("doctor")) : null;
        this.patient = inits.isInitialized("patient") ? new QPatientEntity(forProperty("patient")) : null;
    }

}

