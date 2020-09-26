package com.sound_utils;

import javax.sound.sampled.*;
import java.net.DatagramPacket;

public class Speaker {
    private SourceDataLine speakers;

    public Speaker(AudioFormat format) throws
                                       LineUnavailableException {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);

        speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        speakers.open(format);
        speakers.start();
    }

    public void write(DatagramPacket data) {
        speakers.write(data.getData(), 0, data.getData().length);
    }
}
