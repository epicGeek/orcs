package com.nokia.ices.apps.fusion.log.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(HistoryData.class)
public abstract class HistoryData_ {

	public static volatile SingularAttribute<HistoryData, String> jobOperator;
	public static volatile SingularAttribute<HistoryData, Product> product;
	public static volatile SingularAttribute<HistoryData, String> modular;
	public static volatile SingularAttribute<HistoryData, Stage> stage;
	public static volatile SingularAttribute<HistoryData, Date> logDate;
	public static volatile SingularAttribute<HistoryData, WorkType> workPackageType;
	public static volatile SingularAttribute<HistoryData, WorkPackage> workPackage;
	public static volatile SingularAttribute<HistoryData, Long> id;
	public static volatile SingularAttribute<HistoryData, Double> time;
	public static volatile SingularAttribute<HistoryData, String> userName;
	public static volatile SingularAttribute<HistoryData, Project> projectName;
	public static volatile SingularAttribute<HistoryData, Boolean> status;

}

