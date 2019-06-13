package com.clover.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author dingpengfei
 * @date 2019-05-19 18:42
 */
public class MD5Utils {
    public static String md5Digest(String msg) throws Exception {
        try {
            byte[] buf = msg.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buf);
            byte[] tmp = md5.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : tmp) {
                if (b >= 0 && b < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(b & 0xff));
            }
            System.out.println("md5 length:" + tmp.length + " result length:" + sb.toString().length());
            System.out.println(sb);
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static String getSign(String url) throws NoSuchAlgorithmException {
        String key="WrKroAb-giSNw-vyH-PIvFnfXpKbjTqSa";
        MessageDigest md5Digest=MessageDigest.getInstance("MD5");
        md5Digest.update((url+key).getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : md5Digest.digest()) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString().toLowerCase();
    }
}
