package com.yashmerino.url_shortener.util;

/**
 * Class that manages the Base64 operations.
 */
public class Base62Convertor {
    /**
     * String containing every single character.
     */
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Int representing all characters' length.
     */
    private static final int BASE = CHARACTERS.length();

    /**
     * Encodes a Long value to a Base62 String.
     *
     * @param input is the input value.
     *
     * @return the encoded Base62 String.
     */
    public static String encode(long input) {
        StringBuilder sb = new StringBuilder();
        while (input > 0) {
            sb.append(CHARACTERS.charAt((int) (input % BASE)));
            input /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * Decodes a Base64 String back to its Long value.
     *
     * @param input is the input value.
     *
     * @return the decoded Long value.
     */
    public static long decode(String input) {
        long res = 0;
        for (char c : input.toCharArray()) {
            res = res * BASE + CHARACTERS.indexOf(c);
        }
        return res;
    }
}