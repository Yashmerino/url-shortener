package com.yashmerino.url_shortener.util;

/**
 * Util class that generates short codes using Base62 encoding.
 */
public class HashUtils {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * Private constructor to hide the public one.
     */
    private HashUtils () {

    }

    /**
     * Generates a Base62-encoded short code from an ID.
     *
     * @param id is the database ID of the URL mapping.
     *
     * @return the generated Base62 short code.
     */
    public static String generateShortCode(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(BASE62.charAt((int) (id % 62)));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}