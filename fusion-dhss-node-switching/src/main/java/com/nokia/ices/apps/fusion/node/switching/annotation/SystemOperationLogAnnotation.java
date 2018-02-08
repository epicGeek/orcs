package com.nokia.ices.apps.fusion.node.switching.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mars 2015-04-20
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemOperationLogAnnotation {

	//模块名称
	String appModule();
	
	//操作类型: Login, Add, Remove, Update
	String opType();
	
	//操作类容
	String opText();
}
