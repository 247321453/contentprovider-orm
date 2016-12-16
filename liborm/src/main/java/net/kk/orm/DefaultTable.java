package net.kk.orm;

import net.kk.orm.annotations.Table;

import java.lang.annotation.Annotation;

final class DefaultTable implements Table {
    private Class<?> cls;

    public DefaultTable(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public String value() {
        return cls.getSimpleName();
    }

    @Override
    public String createSql() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Table.class;
    }

    @Override
    public String uri() {
        return null;
    }
}
