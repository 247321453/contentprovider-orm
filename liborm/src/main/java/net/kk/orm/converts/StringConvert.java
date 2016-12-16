package net.kk.orm.converts;


public class StringConvert extends CustomConvert<String> {
    @Override
    public String toValue(String val) {
        return val;
    }

    public StringConvert() {
    }

    @Override
    public String toDbValue(String value) {
        return value;
    }
}
