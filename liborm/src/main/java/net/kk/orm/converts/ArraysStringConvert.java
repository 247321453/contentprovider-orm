package net.kk.orm.converts;


class ArraysStringConvert extends ArraysConvert<String> {
    @Override
    protected String[] createArrays(int len) {
        return new String[len];
    }

    @Override
    protected String toString(String s) {
        if (s != null) {
            s = s.replace(",", "$dot$");
        }
        return super.toString(s);
    }

    @Override
    protected String toT(String val) {
        if (val == null) {
            return null;
        }
        val = val.replace("$dot$", ",");
        return val;
    }
}
