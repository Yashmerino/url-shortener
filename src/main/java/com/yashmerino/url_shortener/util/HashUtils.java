package com.yashmerino.url_shortener.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Util class that generates the MD5 hash.
 */
public class HashUtils {

    /**
     * Generates MD5 hash.
     *
     * @param input is the initial value.
     *
     * @return the generated MD5 hash.
     */
    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return HexFormat.of().formatHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm", e);
        }
    }
}