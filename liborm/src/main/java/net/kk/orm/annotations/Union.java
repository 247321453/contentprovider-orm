package net.kk.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Union {
    String key() default "";
    /***
     * 用于外键触发处理
     */
    boolean readOnly() default false;
}
