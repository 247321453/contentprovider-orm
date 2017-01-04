package net.kk.orm.annotations;


import net.kk.orm.converts.IConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value();

    boolean primaryKey() default false;

    boolean autoIncrement() default false;

    /***
     * 默认值
     */
    String defaultValue() default "___NULL";

    /***
     * 联合的自定义字段，默认是表的keyId
     */
    String unionKey() default "";

    /***
     * 用于外键触发处理
     */
    boolean unionReadOnly() default false;

    /***
     * 自定义转换
     */
    Class<? extends IConvert> convert() default IConvert.class;
}
