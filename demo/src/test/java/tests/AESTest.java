package tests;

import net.kk.orm.demo.crypto.AESHelper;

import org.junit.Test;

public class AESTest {
    @Test
    public void testString() throws Exception {
        AESHelper aesHelper = new AESHelper(AESHelper.METHOD2, false);
        final String key = "123456";
        String str = aesHelper.encrypt(key, "hello");
        System.out.println(str);
        System.out.println(aesHelper.decrypt(key, str));
    }
}
