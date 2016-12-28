package net.kk.orm.converts;


class ArraysDoubleConvert extends ArraysConvert<Double> {
    @Override
    protected Double[] createArrays(int len) {
        return new Double[len];
    }

    @Override
    protected Double toT(String val) {
        return Double.parseDouble(val);
    }
}
