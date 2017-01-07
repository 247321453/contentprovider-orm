package net.kk.orm.converts;


class ArraysIntegerConvert extends ArraysConvert<Integer> {
    @Override
    protected Integer[] createArrays(int len) {
        return new Integer[len];
    }

    @Override
    protected Integer toT(String val) {
        return Integer.parseInt(val);
    }
}
