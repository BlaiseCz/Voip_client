package com.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordUtils {

    public static String getPasswordHash(String plainPassword) {
        return Hashing.sha512()
                      .hashString(plainPassword, StandardCharsets.UTF_8)
                      .toString();
    }

    public static String getSha128(String text) {
        return Hashing.murmur3_128()
                      .hashString(text, StandardCharsets.UTF_8)
                      .toString();
    }
}
