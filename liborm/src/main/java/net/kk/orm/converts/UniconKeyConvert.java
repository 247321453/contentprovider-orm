package net.kk.orm.converts;

import net.kk.orm.api.SQLiteOpera;
import net.kk.orm.linq.Orm;
import net.kk.orm.api.OrmColumn;
import net.kk.orm.api.SQLiteType;

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
//        Log.d("orm", "class=" + pClass + ",id=" + id);
        T t = orm.select(pClass).where(column.getColumnName(), "=", id).findFirst();
//        Log.d("orm", "to T " + t);
        return t;
    }

    @Override
    public D toDbValue(Orm orm, T value, int opera) {
        Object id = column.getValue(value);
        if (column.isEvent(opera)) {
            try {
                if ((opera & SQLiteOpera.INSERT) == SQLiteOpera.INSERT) {
                    orm.insert(value);
                } else if ((opera & SQLiteOpera.UPDATE) == SQLiteOpera.UPDATE) {
                    orm.update(value);
                } else if ((opera & SQLiteOpera.DELETE) == SQLiteOpera.DELETE) {
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
