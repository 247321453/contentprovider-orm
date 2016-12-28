package net.kk.orm.converts;


class ArraysFloatConvert extends ArraysConvert<Float> {
    @Override
    protected Float[] createArrays(int len) {
        return new Float[len];
    }

    @Override
    protected Float toT(String val) {
        return Float.parseFloat(val);
    }
}
