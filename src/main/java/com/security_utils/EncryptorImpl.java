package com.security_utils;

import com.models.MicrophoneData;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class EncryptorImpl implements Encryptor {
    private String key;

    @Override
    @SneakyThrows
    public MicrophoneData encrypt(MicrophoneData data) {
        byte[] encrypt = new EncryptionUtils(key).encrypt(data.getData());
        return new MicrophoneData(encrypt, encrypt.length);
    }
}
