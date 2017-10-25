package info.xiaomo.corelogger.annotation;


import info.xiaomo.corelogger.TableCycle;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Table {
	
	String primaryKey() default "id";

	TableCycle cycle() default TableCycle.SINGLE;
	
	String tableName() default "";
}
