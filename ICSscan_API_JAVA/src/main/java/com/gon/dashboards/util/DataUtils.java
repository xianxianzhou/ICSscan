package com.gon.dashboards.util;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DataUtils {


    public static String codeToHash(String code) throws NoSuchAlgorithmException {
        byte[] byteTx = Base64.getDecoder().decode(code);
        String hash = String2SHA256StrJava(byteTx);
        return hash;
    }

    public static String String2SHA256StrJava(byte[] byteTx) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        String encodeStr = "";
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(byteTx);
        encodeStr = byte2Hex(messageDigest.digest());
        return encodeStr;
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    public static byte[] hex2byte(String hash) {
        char[] data = hash.toCharArray();
        final int len = data.length;
        final byte[] out = new byte[len >> 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j]) << 4;
            j++;
            f = f | toDigit(data[j]);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private static int toDigit(final char ch) {
        final int digit = Character.digit(ch, 16);
        return digit;
    }
}
