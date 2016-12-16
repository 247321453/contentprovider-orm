package net.kk.orm.demo.sec;


import net.kk.orm.converts.StringConvert;
import net.kk.orm.demo.crypto.DESUtils;

public class SecConvert extends StringConvert {
    private final String TAG = "StringConvert";

    @Override
    public String toValue(String val) {
        //解密
        return DESUtils.decrypt(val, TAG);
    }

    @Override
    public String toDbValue(String value) {
        //加密
        return DESUtils.encrypt(value, TAG);
    }
}
