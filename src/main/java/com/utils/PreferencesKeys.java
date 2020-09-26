package com.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PreferencesKeys {
    PASSWORD_HASH("password_hash_key", String.class),
    USERNAME("username_key", String.class),
    REMEMBER_ME("remember_me_key", Boolean.class);

    private String key;
    private Class aClass;
}
