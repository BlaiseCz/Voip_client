package com.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MicrophoneData {
    private byte[] data;
    private int numBytesRead;
}
