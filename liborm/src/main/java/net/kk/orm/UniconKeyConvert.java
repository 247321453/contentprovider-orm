package net.kk.orm;

import net.kk.orm.converts.IConvert;
import net.kk.orm.enums.SQLiteOpera;
import net.kk.orm.enums.SQLiteType;

class UniconKeyConvert<D, T> implements IConvert<D, T> {
    private Class<T> pClass;
    private Class<D> idClass;
    private OrmColumn column;

    public UniconKeyConvert(Class<T> pClass, Class<D> idClass, OrmColumn column) {
        this.pClass = pClass;
        this.idClass = idClass;
        this.column = column;
    }

    @Override
    public SQLiteType getSQLiteType() {
        return column.getSQLiteType();
    }

    @Override
    public T toValue(Orm orm, D id) {
        //根据id查找
        T t = orm.select(pClass).where(column.getColumnName(), "=", id).findFirst();
        return t;
    }

    @Override
    public D toDbValue(Orm orm, T value, SQLiteOpera opera) {
        Object id = column.getValue(value);
        if (!column.isReadOnly()) {
            try {
                if (opera == SQLiteOpera.INSERT || opera == SQLiteOpera.UPDATE) {
                    orm.replace(value);
                } else if (opera == SQLiteOpera.DELETE) {
                    orm.delete(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        D d = (D) column.toDbValue(orm, id, opera);
        return d;
    }
}
