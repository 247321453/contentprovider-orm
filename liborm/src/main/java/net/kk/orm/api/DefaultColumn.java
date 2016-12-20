package net.kk.orm.api;


import net.kk.orm.annotations.Column;
import net.kk.orm.converts.IConvert;
import net.kk.orm.enums.SQLiteOpera;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

final class DefaultColumn implements Column {
    private Field mField;

    public DefaultColumn(Field field) {
        mField = field;
    }

    @Override
    public String value() {
        return mField.getName();
    }

    @Override
    public String unionName() {
        return null;
    }

    @Override
    public boolean primaryKey() {
        return false;
    }

    @Override
    public boolean autoIncrement() {
        return false;
    }

    @Override
    public String defaultValue() {
        return null;
    }

    @Override
    public int eventFlags() {
        return SQLiteOpera.INSERT|SQLiteOpera.UPDATE|SQLiteOpera.DELETE;
    }

    @Override
    public Class<? extends IConvert> convert() {
        return IConvert.class;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Column.class;
    }
}
