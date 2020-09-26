package com.security_utils;

import java.net.DatagramPacket;

public interface Decryptor {
    DatagramPacket decrypt(DatagramPacket data);
}
