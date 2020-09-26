package com.runners;

import com.models.ConnectionDetails;
import com.security_utils.Encryptor;
import com.security_utils.EncryptorImpl;
import com.sound_utils.Microphone;
import com.udp_communication.SingleClientVoiceSender;
import com.udp_communication.VoiceSender;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.InetAddress;

public class SenderRunner {

    public static void main(String[] args) throws
                                           IOException,
                                           LineUnavailableException {
        String ip = "127.0.0.1";

        Encryptor encryptor = new EncryptorImpl("testowyklucz");

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        ConnectionDetails receiver = new ConnectionDetails(InetAddress.getByName(ip), 5555, 1024);
        Microphone microphone = new Microphone(format);

        VoiceSender sender = new SingleClientVoiceSender(receiver, microphone, encryptor);
        sender.startSending();
    }
}
