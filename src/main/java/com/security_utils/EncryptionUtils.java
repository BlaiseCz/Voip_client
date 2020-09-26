package com.security_utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {
    private String cipherInstance;
    private SecretKeySpec secretKey;
    private Cipher myCipher;

    public EncryptionUtils(String key) throws
                                       NoSuchPaddingException,
                                       NoSuchAlgorithmException {
        this.cipherInstance = "AES/ECB/PKCS5Padding";

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes());
        byte[] digest = md.digest();

        key = DatatypeConverter
                .printHexBinary(digest)
                .toUpperCase();

        this.secretKey = new SecretKeySpec(key.getBytes(), "AES");

        this.myCipher = Cipher.getInstance(cipherInstance);
    }

    public byte[] encrypt(byte[] message) throws
                                          InvalidKeyException,
                                          BadPaddingException,
                                          IllegalBlockSizeException {
        myCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return myCipher.doFinal(message);
    }

    public byte[] decrypt(byte[] message) throws
                                          InvalidKeyException,
                                          BadPaddingException,
                                          IllegalBlockSizeException {
        myCipher.init(Cipher.DECRYPT_MODE, secretKey);
        return myCipher.doFinal(message);
    }
}
