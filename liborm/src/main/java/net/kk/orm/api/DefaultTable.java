package net.kk.orm.api;

import net.kk.orm.annotations.Table;

import java.lang.annotation.Annotation;

final class DefaultTable implements Table {
    private Class<?> cls;

    public DefaultTable(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public String name() {
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
    public boolean readOnly() {
        return false;
    }

    @Override
    public String typeName() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }
}
