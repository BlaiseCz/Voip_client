package com.sound_utils;

import com.models.MicrophoneData;

import javax.sound.sampled.*;

public class Microphone {
    private TargetDataLine microphoneLine;
    private static final int CHUNK_SIZE = 1024;

    public Microphone(AudioFormat format) throws
                             LineUnavailableException {
        microphoneLine = AudioSystem.getTargetDataLine(format);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphoneLine = (TargetDataLine) AudioSystem.getLine(info);
        microphoneLine.open(format);

        microphoneLine.start();
    }

    public MicrophoneData read() {
        byte[] data = new byte[CHUNK_SIZE];
        int numBytesRead = microphoneLine.read(data, 0, CHUNK_SIZE);

        return new MicrophoneData(data, numBytesRead);
    }
}
