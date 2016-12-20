package net.kk.orm.converts;


import net.kk.orm.api.Orm;
import net.kk.orm.enums.SQLiteType;

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
    public Integer toDbValue(Orm orm,Boolean value,int opera) {
        return value == Boolean.TRUE ? 1 : 0;
    }
}
