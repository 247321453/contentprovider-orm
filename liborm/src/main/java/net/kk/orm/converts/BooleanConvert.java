package net.kk.orm.converts;


import net.kk.orm.api.SQLiteOpera;
import net.kk.orm.linq.Orm;
import net.kk.orm.api.SQLiteType;

class BooleanConvert implements IConvert<Integer, Boolean> {
    public final static SQLiteType TYPE = SQLiteType.INTEGER;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public BooleanConvert() {
    }

    @Override
    public Boolean toValue(Orm orm,Integer val) {
        return val != null && val > 0;
    }

    @Override
    public Integer toDbValue(Orm orm,Boolean value,SQLiteOpera opera) {
        return value == Boolean.TRUE ? 1 : 0;
    }
}
