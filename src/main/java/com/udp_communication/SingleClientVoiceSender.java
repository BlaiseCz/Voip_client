package com.udp_communication;

import com.models.ConnectionDetails;
import com.models.MicrophoneData;
import com.security_utils.Encryptor;
import com.sound_utils.Microphone;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;

public class SingleClientVoiceSender implements VoiceSender {
    private static final Logger logger = Logger.getLogger(SingleClientVoiceSender.class);

    private ConnectionDetails connectionDetails;
    private DatagramSocket socket;
    private Microphone microphone;
    private boolean sendVoice = true;
    private boolean establishedConnection = false;

    private Encryptor encryption;

    public SingleClientVoiceSender(ConnectionDetails connectionDetails, Microphone microphone) throws
                                                                SocketException {
        this.connectionDetails = connectionDetails;
        this.socket = new DatagramSocket();
        this.microphone = microphone;
    }

    public SingleClientVoiceSender(ConnectionDetails connectionDetails, Microphone microphone, Encryptor encryptor) throws
                                                                                                                 SocketException {
        this.connectionDetails = connectionDetails;
        this.socket = new DatagramSocket();
        this.microphone = microphone;
        this.encryption = encryptor;
    }

    @Override
    public void startSending() {
        establishedConnection = true;
        sendVoice = true;

        new Thread(() -> {
            while (establishedConnection) {
                if (!sendVoice) {
                    continue;
                }

                MicrophoneData microphoneData = microphone.read();

                if (Objects.nonNull(encryption)) {
                    microphoneData = encryption.encrypt(microphoneData);
                }

                byte[] data = microphoneData.getData();
                int numBytesRead = microphoneData.getNumBytesRead();

                DatagramPacket request = new DatagramPacket(data, numBytesRead, connectionDetails.getHostUrl(), connectionDetails.getPort());

                try {
                    socket.send(request);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void pauseSending() {
        this.sendVoice = false;
    }

    @Override
    public void resumeSending() {
        this.sendVoice = true;
    }


    @Override
    public void stopSending() {
        establishedConnection = false;
        sendVoice = false;
        socket.close();
    }
}
