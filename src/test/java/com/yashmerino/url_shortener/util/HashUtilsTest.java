package com.yashmerino.url_shortener.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashUtilsTest {

    @Test
    void generateShortCode_ShouldReturnValidBase62() {
        Long testId = 123L;
        String shortCode = HashUtils.generateShortCode(testId);
        
        assertNotNull(shortCode);
        assertFalse(shortCode.isEmpty());
        assertTrue(shortCode.matches("[0-9A-Za-z]+"));
    }

    @Test
    void generateShortCode_ShouldReturnTheSameCodeForSameId() {
        Long testId = 456L;
        String shortCode1 = HashUtils.generateShortCode(testId);
        String shortCode2 = HashUtils.generateShortCode(testId);
        
        assertEquals(shortCode1, shortCode2);
    }

    @Test
    void generateShortCode_ShouldReturnDifferentCodesForDifferentIds() {
        String shortCode1 = HashUtils.generateShortCode(1L);
        String shortCode2 = HashUtils.generateShortCode(2L);
        
        assertNotEquals(shortCode1, shortCode2);
    }

    @Test
    void generateShortCode_ShouldThrowExceptionForNegativeId() {
        assertThrows(IllegalArgumentException.class, () -> HashUtils.generateShortCode(-1L));
    }

    @Test
    void generateShortCode_ShouldThrowExceptionForZeroId() {
        assertThrows(IllegalArgumentException.class, () -> HashUtils.generateShortCode(0L));
    }

    @Test
    void generateShortCode_ShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> HashUtils.generateShortCode(null));
    }
}

