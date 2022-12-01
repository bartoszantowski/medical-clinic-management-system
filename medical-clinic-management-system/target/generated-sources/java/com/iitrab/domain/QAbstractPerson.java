package com.iitrab.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractPerson is a Querydsl query type for AbstractPerson
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractPerson extends EntityPathBase<AbstractPerson> {

    private static final long serialVersionUID = 1237884287L;

    public static final QAbstractPerson abstractPerson = new QAbstractPerson("abstractPerson");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath firstName = createString("firstName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath lastName = createString("lastName");

    public final StringPath telephoneNumber = createString("telephoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QAbstractPerson(String variable) {
        super(AbstractPerson.class, forVariable(variable));
    }

    public QAbstractPerson(Path<? extends AbstractPerson> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractPerson(PathMetadata metadata) {
        super(AbstractPerson.class, metadata);
    }

}

