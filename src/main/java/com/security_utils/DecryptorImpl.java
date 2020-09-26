package com.security_utils;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.net.DatagramPacket;

@AllArgsConstructor
public class DecryptorImpl implements Decryptor {
    private String key;

    @Override
    @SneakyThrows
    public DatagramPacket decrypt(DatagramPacket data) {
        byte[] decrypt = new EncryptionUtils(key).decrypt(data.getData());
        return new DatagramPacket(decrypt, decrypt.length);
    }
}
