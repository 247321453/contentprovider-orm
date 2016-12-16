package net.kk.orm.utils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class StringUtils {
    public static String join(Object o, String flag) {
        if (o != null) {
            if (o.getClass().isArray()) {
                StringBuffer str_buff = new StringBuffer();
                int length = Array.getLength(o);
                for (int i = 0, len = length; i < len; i++) {
                    str_buff.append(String.valueOf(Array.get(o, i)));
                    if (i < len - 1) str_buff.append(flag);
                }
                return str_buff.toString();
            }
        }
        return null;
    }

    public static String join(Object[] o, String flag) {
        StringBuffer str_buff = new StringBuffer();
        for (int i = 0, len = o.length; i < len; i++) {
            str_buff.append(String.valueOf(o[i]));
            if (i < len - 1) str_buff.append(flag);
        }
        return str_buff.toString();
    }
}
