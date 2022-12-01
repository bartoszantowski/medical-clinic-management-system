package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDutyEntity is a Querydsl query type for DutyEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDutyEntity extends EntityPathBase<DutyEntity> {

    private static final long serialVersionUID = -209302079L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDutyEntity dutyEntity = new QDutyEntity("dutyEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QDoctorEntity doctor;

    public final DateTimePath<java.time.LocalDateTime> endDutyDate = createDateTime("endDutyDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final DateTimePath<java.time.LocalDateTime> startDutyDate = createDateTime("startDutyDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QDutyEntity(String variable) {
        this(DutyEntity.class, forVariable(variable), INITS);
    }

    public QDutyEntity(Path<? extends DutyEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDutyEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDutyEntity(PathMetadata metadata, PathInits inits) {
        this(DutyEntity.class, metadata, inits);
    }

    public QDutyEntity(Class<? extends DutyEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.doctor = inits.isInitialized("doctor") ? new QDoctorEntity(forProperty("doctor")) : null;
    }

}

