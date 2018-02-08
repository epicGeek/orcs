package com.nokia.ices.apps.fusion.log.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WorkType.class)
public abstract class WorkType_ {

	public static volatile SingularAttribute<WorkType, String> workType;
	public static volatile SingularAttribute<WorkType, Long> id;
	public static volatile SetAttribute<WorkType, WorkPackage> items;

}

