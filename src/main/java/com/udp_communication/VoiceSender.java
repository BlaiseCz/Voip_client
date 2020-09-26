package com.udp_communication;

public interface VoiceSender {
    void startSending();

    void pauseSending();

    void resumeSending();
    void stopSending();
}
