package info.xiaomo.core.logger.annotation;


import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import info.xiaomo.core.logger.FieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
@Inherited
public @interface Column {
	
	FieldType fieldType();

	String commit() default "";
	
	boolean allowNull() default false;
	
	boolean autoIncrement() default false;
	
	int size() default 0;
	
	String colName() default "";
}
