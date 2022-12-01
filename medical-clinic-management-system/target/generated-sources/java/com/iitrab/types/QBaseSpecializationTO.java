package com.iitrab.types;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseSpecializationTO is a Querydsl query type for BaseSpecializationTO
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QBaseSpecializationTO extends EntityPathBase<BaseSpecializationTO> {

    private static final long serialVersionUID = 256081550L;

    public static final QBaseSpecializationTO baseSpecializationTO = new QBaseSpecializationTO("baseSpecializationTO");

    public final EnumPath<com.iitrab.domain.SpecializationType> doctorSpecializationType = createEnum("doctorSpecializationType", com.iitrab.domain.SpecializationType.class);

    public final DateTimePath<java.time.LocalDateTime> visitDate = createDateTime("visitDate", java.time.LocalDateTime.class);

    public QBaseSpecializationTO(String variable) {
        super(BaseSpecializationTO.class, forVariable(variable));
    }

    public QBaseSpecializationTO(Path<? extends BaseSpecializationTO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseSpecializationTO(PathMetadata metadata) {
        super(BaseSpecializationTO.class, metadata);
    }

}

