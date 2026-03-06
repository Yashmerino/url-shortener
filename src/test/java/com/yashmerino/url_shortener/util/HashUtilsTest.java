package com.yashmerino.url_shortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashUtilsTest {
    private final String testValue = "value";

    @Test
    void generateMD5_ShouldReturnHash() {
        final String generatedHash = HashUtils.generateMD5(testValue);
        assertEquals("2063c1608d6e0baf80249c42e2be5804", generatedHash);
    }

    @Test
    void generateMD5_ShouldReturnTheSameHash() {
        String generatedHash = HashUtils.generateMD5(testValue);
        assertEquals("2063c1608d6e0baf80249c42e2be5804", generatedHash);

        generatedHash = HashUtils.generateMD5(testValue);
        assertEquals("2063c1608d6e0baf80249c42e2be5804", generatedHash);
    }
}
