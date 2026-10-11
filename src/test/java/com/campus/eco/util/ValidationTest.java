package com.campus.eco.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidationTest {

    @Test
    void checksBlankValues() {
        assertFalse(Validation.notBlank(null));
        assertFalse(Validation.notBlank(" \t "));
        assertTrue(Validation.notBlank(" value "));
    }

    @Test
    void acceptsOnlyUnsignedWholeNumbers() {
        assertTrue(Validation.isInt(" 42 "));
        assertFalse(Validation.isInt(null));
        assertFalse(Validation.isInt("-42"));
        assertFalse(Validation.isInt("4.2"));
    }

    @Test
    void acceptsWholeNumbersAndDecimalsWithDigitsOnBothSides() {
        assertTrue(Validation.isDecimal("42"));
        assertTrue(Validation.isDecimal(" 4.25 "));
        assertFalse(Validation.isDecimal(".25"));
        assertFalse(Validation.isDecimal("4."));
        assertFalse(Validation.isDecimal("4.2.5"));
    }

    @Test
    void validatesEmailShape() {
        assertTrue(Validation.isEmail("student.name@example.edu"));
        assertFalse(Validation.isEmail("student@example"));
        assertFalse(Validation.isEmail("student @example.edu"));
        assertFalse(Validation.isEmail(null));
    }

    @Test
    void safelyTrimsInputAndConvertsNullToEmpty() {
        assertEquals("value", Validation.safe(" value "));
        assertEquals("", Validation.safe(null));
    }
}
