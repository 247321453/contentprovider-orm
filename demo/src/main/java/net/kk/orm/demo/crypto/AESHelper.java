package net.kk.orm.demo.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {
    public static final String TAG = AESHelper.class.getSimpleName();

    private static final String AES = "AES";
    private static final String SHA1 = "SHA1PRNG";
    public static final String METHOD = "AES/CFB/NoPadding";
    public static final String METHOD2 = "AES/CBC/PKCS5Padding";
    private static final int MIN_KEY_LENGTH = 16;
    private String mMethod;
    private boolean useCrypto;

    public AESHelper(String method, boolean useCrypto) {
        mMethod = method;
        this.useCrypto = useCrypto;
    }

    public void decrypt(String seed, InputStream inputStream, OutputStream outputStream)
            throws Exception {
        Cipher cipher = Cipher.getInstance(mMethod);
        SecretKeySpec secretKey = new SecretKeySpec(makeKey(seed.getBytes()), AES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

        byte[] data = new byte[1024];
        int len;
        while ((len = inputStream.read(data)) != -1) {
            cipherOutputStream.write(data, 0, len);
        }
        cipherOutputStream.flush();
        cipherOutputStream.close();
    }

    public void encrypt(String seed, InputStream inputStream, OutputStream outputStream)
            throws Exception {
        Cipher cipher = Cipher.getInstance(mMethod);
        SecretKeySpec secretKey = new SecretKeySpec(makeKey(seed.getBytes()), AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
        byte[] data = new byte[1024];
        int len;
        while ((len = cipherInputStream.read(data)) != -1) {
            outputStream.write(data, 0, len);
        }
        outputStream.flush();
        cipherInputStream.close();
    }


    private byte[] makeKey(byte[] key) throws Exception {
        if (key == null) {
            throw new Exception("key not is null");
        }
        return MD5Utils.md5Bytes(key);
    }

    /**
     * 加密后的字符串
     *
     * @param seed
     * @return
     */
    public String encrypt(String seed, String source) {
        // Log.d(TAG, "加密前的seed=" + seed + ",内容为:" + clearText);
        byte[] result = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayInputStream = new ByteArrayInputStream(source.getBytes());
            encrypt(seed, byteArrayInputStream, byteArrayOutputStream);
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOutputStream);
            close(byteArrayInputStream);
        }
        return toHex(result);
    }

    /**
     * 解密后的字符串
     *
     * @param seed
     * @param encrypted
     * @return
     */
    public String decrypt(String seed, String encrypted) {
        if (encrypted == null) return null;
        byte[] result = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayInputStream = new ByteArrayInputStream(toByte(encrypted));
            decrypt(seed, byteArrayInputStream, byteArrayOutputStream);
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOutputStream);
            close(byteArrayInputStream);
        }
        return result == null ? null : new String(result);
    }

    /**
     * 结合密钥生成加密后的密文
     *
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String seed, byte[] input) {
        if (input == null) return null;
        byte[] result = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayInputStream = new ByteArrayInputStream(input);
            encrypt(seed, byteArrayInputStream, byteArrayOutputStream);
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOutputStream);
            close(byteArrayInputStream);
        }
        return result;
    }

    /**
     * 根据密钥解密已经加密的数据
     */
    public byte[] decrypt(String seed, byte[] encrypted) {
        if (encrypted == null) return null;
        byte[] result = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayInputStream = new ByteArrayInputStream(encrypted);
            decrypt(seed, byteArrayInputStream, byteArrayOutputStream);
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(byteArrayOutputStream);
            close(byteArrayInputStream);
        }
        return result;
    }

    private static byte[] toByte(String hexString) {
        return Base64.decode(hexString);
    }

    private static String toHex(byte[] buf) {
        if (buf == null) return null;
        return Base64.encodeToString(buf);
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}