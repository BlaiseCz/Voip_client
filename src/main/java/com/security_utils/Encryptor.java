package com.security_utils;

import com.models.MicrophoneData;

public interface Encryptor {
    MicrophoneData encrypt(MicrophoneData data);
}
