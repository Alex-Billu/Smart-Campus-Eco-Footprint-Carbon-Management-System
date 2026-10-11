package com.campus.eco.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {

    @Test
    void hashesInputDeterministicallyAsSha256Hex() {
        String hash = PasswordUtil.hash("campus-password");

        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]{64}"));
        assertEquals(hash, PasswordUtil.hash("campus-password"));
    }

    @Test
    void returnsEmptyHashForNullInput() {
        assertEquals("", PasswordUtil.hash(null));
    }

    @Test
    void verifiesMatchingHashAndRejectsDifferentPassword() {
        String hash = PasswordUtil.hash("campus-password");

        assertTrue(PasswordUtil.verify("campus-password", hash));
        assertFalse(PasswordUtil.verify("different-password", hash));
    }

    @Test
    void comparesHexadecimalHashWithoutCaseSensitivity() {
        String hash = PasswordUtil.hash("campus-password");

        assertTrue(PasswordUtil.verify("campus-password", hash.toUpperCase()));
    }
}
