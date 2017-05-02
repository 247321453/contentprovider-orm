package net.kk.orm.demo.crypto;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String md5(byte[] source) {
        String hash = null;
        ByteArrayInputStream in = null;
        try {
            in = new ByteArrayInputStream(source);
            hash = md52String(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return hash;
    }

    public static String md5(String source) {
        return md5(source.getBytes());
    }

    public static String md5File(String file) {
        return md5(new File(file));
    }

    /**
     * 获取文件的MD5
     */
    public static String md5(File file) {
        String hash = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            hash = md52String(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return hash;
    }

    public static byte[] md5Bytes(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        byte[] result = null;
        try {
            result = md5(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] md5(InputStream stream) throws NoSuchAlgorithmException, IOException {
        String hash = null;
        byte[] buffer = new byte[1024];
        BufferedInputStream in = null;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        in = new BufferedInputStream(stream);
        int numRead = 0;
        while ((numRead = in.read(buffer)) > 0) {
            md5.update(buffer, 0, numRead);
        }
        return md5.digest();
    }

    private static String md52String(InputStream stream) throws NoSuchAlgorithmException, IOException {
        return ByteUtils.toHexString(md5(stream));
    }
}
