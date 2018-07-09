package org.scoreboard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
	public static final String DEFAULT_DATASOURCE = "dataSource";

	public static final String DEFAULT_DATASOURCE_CLONE = "dataSourceClone";

	String value() default DEFAULT_DATASOURCE;
}
