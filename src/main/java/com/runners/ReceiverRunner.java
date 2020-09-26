package com.runners;

import com.models.ConnectionDetails;
import com.security_utils.Decryptor;
import com.security_utils.DecryptorImpl;
import com.sound_utils.Speaker;
import com.udp_communication.VoiceReceiver;
import com.udp_communication.VoiceReceiverImpl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ReceiverRunner {

    public static void main(String[] args) throws
                                           UnknownHostException,
                                           LineUnavailableException,
                                           SocketException {
        Decryptor decryptor = new DecryptorImpl("b38f8c68806e0099e6a95a85295183b3e5f2f50b5e52dc1e71ad4571e5c869e8");

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        ConnectionDetails sender = new ConnectionDetails(InetAddress.getByName("localhost"), 5555, 1024);
        Speaker speaker = new Speaker(format);

        VoiceReceiver voiceReceiver = new VoiceReceiverImpl(sender, speaker, decryptor);
        voiceReceiver.startListening();
    }
}
