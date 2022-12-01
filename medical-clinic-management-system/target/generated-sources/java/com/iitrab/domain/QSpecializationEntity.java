package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpecializationEntity is a Querydsl query type for SpecializationEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSpecializationEntity extends EntityPathBase<SpecializationEntity> {

    private static final long serialVersionUID = -28555594L;

    public static final QSpecializationEntity specializationEntity = new QSpecializationEntity("specializationEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ListPath<DoctorEntity, QDoctorEntity> doctors = this.<DoctorEntity, QDoctorEntity>createList("doctors", DoctorEntity.class, QDoctorEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<SpecializationType> specializationType = createEnum("specializationType", SpecializationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QSpecializationEntity(String variable) {
        super(SpecializationEntity.class, forVariable(variable));
    }

    public QSpecializationEntity(Path<? extends SpecializationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecializationEntity(PathMetadata metadata) {
        super(SpecializationEntity.class, metadata);
    }

}

