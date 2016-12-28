package net.kk.orm.converts;


class ArraysLongConvert extends ArraysConvert<Long> {
    @Override
    protected Long[] createArrays(int len) {
        return new Long[len];
    }

    @Override
    protected Long toT(String val) {
        return Long.parseLong(val);
    }
}
