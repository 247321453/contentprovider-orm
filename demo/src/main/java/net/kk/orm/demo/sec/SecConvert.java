package net.kk.orm.demo.sec;


import net.kk.orm.api.Orm;
import net.kk.orm.converts.CustomConvert;
import net.kk.orm.demo.crypto.DESUtils;

public class SecConvert extends CustomConvert<String> {
    private final String TAG = "StringConvert";

    public SecConvert() {

    }

    @Override
    public String toValue(Orm orm, String val) {
        //解密
        return DESUtils.decrypt(val, TAG);
    }

    @Override
    public String toDbValue(Orm orm, String value,int opera) {
        //加密
        return DESUtils.encrypt(value, TAG);
    }
}
