package com.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.InetAddress;

@Data
@Builder
@AllArgsConstructor
public class ConnectionDetails {
    private InetAddress hostUrl;
    private int port;

    private int voicePackageLength;
}
