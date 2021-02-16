package info.xiaomo.gengine.ai.btree.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This field level annotation defines a task attribute.
 *
 * @author davebaol
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TaskAttribute {

  /**
   * Specifies the attribute's name; if empty the name of the field is used instead.
   *
   * @return the attribute's name or an empty string if the name of the field must be used.
   */
  String name() default "";

  /**
   * Specifies whether the attribute is required or not.
   *
   * @return {@code true} if the attribute is required; {@code false} if it is optional.
   */
  boolean required() default false;
}
