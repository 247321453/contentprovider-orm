package net.kk.orm.annotations;

import net.kk.orm.converts.IConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnConvert {
    Class<? extends IConvert> value();
}
