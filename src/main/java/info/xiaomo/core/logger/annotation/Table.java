package info.xiaomo.core.logger.annotation;


import java.lang.annotation.*;
import info.xiaomo.core.logger.TableCycle;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface Table {
	
	String primaryKey() default "id";

	TableCycle cycle() default TableCycle.SINGLE;
	
	String tableName() default "";
}
