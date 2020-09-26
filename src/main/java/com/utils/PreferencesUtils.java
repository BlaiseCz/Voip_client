package com.utils;

import java.util.prefs.Preferences;

public class PreferencesUtils {
    private static final Preferences prefs = Preferences.userNodeForPackage(PreferencesUtils.class);

    public static <T> T getValue(PreferencesKeys key) throws
                                                      Exception {
        if (String.class.equals(key.getAClass())) {
            return (T) prefs.get(key.getKey(), "");
        } else if (Boolean.class.equals(key.getAClass())) {
            return (T) new Boolean(prefs.getBoolean(key.getKey(), false));
        }

        throw new Exception("There is no matching class");
    }

    public static void setValue(PreferencesKeys key, Object value) throws
                                                                   Exception {
        if (String.class.equals(key.getAClass())) {
            String val = (String) value;
            prefs.put(key.getKey(), val);
        } else if (Boolean.class.equals(key.getAClass())) {
            boolean val = Boolean.valueOf((Boolean) value);
            prefs.putBoolean(key.getKey(), val);
        } else {
            throw new Exception("There is no matching class");
        }
    }
}
