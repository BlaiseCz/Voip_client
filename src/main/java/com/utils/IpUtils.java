package com.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class IpUtils {
    private IpUtils() {}

    public static String getLocalIpAddr() throws
            IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("google.com", 80));
        return socket.getLocalAddress().getHostAddress();
    }
}
