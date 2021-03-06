package net.kk.orm.demo.crypto;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtils {
    private final static String DES = "DES";
    /***
     * 获取key
     *
     * @param k
     * @return
     */
    private static String getKey(String k) {
        return ByteUtils.toHexString(k.getBytes());
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) {
        byte[] keys = getKey(key).getBytes();
        byte[] bt = encrypt(data.getBytes(), keys);
        byte[] encodeBase64 = Base64.encode(bt);
        return new String(encodeBase64);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) {
        if (data == null)
            return null;
        byte[] encodeBase64 = Base64.decode(data.getBytes());
        byte[] keys = getKey(key).getBytes();
        byte[] bt = decrypt(encodeBase64, keys);
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    @SuppressWarnings("GetInstance")
    private static byte[] encrypt(byte[] data, byte[] key) {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        byte[] result = data;
        try {
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            result = cipher.doFinal(data);
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    @SuppressWarnings("GetInstance")
    private static byte[] decrypt(byte[] data, byte[] key) {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        byte[] result = data;
        try {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            result = cipher.doFinal(data);
        } catch (Exception e) {

        }
        return result;
    }
}