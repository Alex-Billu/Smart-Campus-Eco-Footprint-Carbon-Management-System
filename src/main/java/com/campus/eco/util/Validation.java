package com.campus.eco.util;

import java.util.regex.Pattern;

public class Validation {
    public static boolean notBlank(String s) { return s != null && !s.trim().isEmpty(); }
    public static boolean isInt(String s) { return notBlank(s) && Pattern.matches("\\d+", s.trim()); }
    public static boolean isDecimal(String s) { return notBlank(s) && Pattern.matches("\\d+(\\.\\d+)?", s.trim()); }
    public static boolean isEmail(String s) {
        return notBlank(s) && Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", s.trim());
    }
    public static String safe(String s) { return s == null ? "" : s.trim(); }
}
