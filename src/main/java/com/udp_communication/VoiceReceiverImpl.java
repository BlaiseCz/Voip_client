package com.udp_communication;

import com.models.ConnectionDetails;
import com.security_utils.Decryptor;
import com.sound_utils.Speaker;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Objects;

public class VoiceReceiverImpl implements VoiceReceiver {
    private static final Logger logger = Logger.getLogger(VoiceReceiverImpl.class);

    private ConnectionDetails connectionDetails;
    private DatagramSocket serverSocket;
    private Speaker speaker;
    private boolean receiveVoice = true;
    private Decryptor decryptor;

    public VoiceReceiverImpl(ConnectionDetails connectionDetails, Speaker speaker) throws
                                                                                   SocketException {
        this.connectionDetails = connectionDetails;
        this.speaker = speaker;
        this.serverSocket = new DatagramSocket(connectionDetails.getPort());
    }

    public VoiceReceiverImpl(ConnectionDetails connectionDetails, Speaker speaker, Decryptor decryptor) throws
                                                                                                        SocketException {
        this.connectionDetails = connectionDetails;
        this.speaker = speaker;
        this.serverSocket = new DatagramSocket(connectionDetails.getPort());
        this.decryptor = decryptor;
    }

    @Override
    public void startListening() {
        receiveVoice = true;

        new Thread(() -> {
            while (receiveVoice) {
                byte[] buffer = new byte[connectionDetails.getVoicePackageLength() + 16];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);

                try {
                    serverSocket.receive(response);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

                if (Objects.nonNull(decryptor)) {
                    try {
                        response = decryptor.decrypt(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                speaker.write(response);
            }
        }).start();
    }

    @Override
    public void pauseListening() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void stopListening() {
        receiveVoice = false;
        serverSocket.close();
    }
}
