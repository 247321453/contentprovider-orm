package net.kk.orm.converts;


import net.kk.orm.SQLiteType;
import net.kk.orm.converts.IConvert;

public class BytesConvert implements IConvert<byte[], byte[]> {
    public final static SQLiteType TYPE = SQLiteType.BLOB;
    @Override
    public SQLiteType getSQLiteType() {
        return TYPE;
    }

    public BytesConvert() {
    }

    @Override
    public byte[] toValue(byte[] value) {
        return value;
    }

    @Override
    public byte[] toDbValue(byte[] value) {
        return value;
    }
}
