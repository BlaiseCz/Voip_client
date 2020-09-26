package microphone_tests;

import com.models.MicrophoneData;
import com.security_utils.DecryptorImpl;
import com.security_utils.EncryptorImpl;
import com.sound_utils.Microphone;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.net.DatagramPacket;

public class EncryptorTest {

    @Test
    public void encryptorTest() {
        String key = "to jestjakistamklucz";

        byte[] dataToEncrypt = "data to encrypt".getBytes();
        MicrophoneData microphoneDataBeforeEncryption = new MicrophoneData(dataToEncrypt, dataToEncrypt.length);

        MicrophoneData microphoneDataEncrypted = new EncryptorImpl(key)
                .encrypt(microphoneDataBeforeEncryption);

        DatagramPacket datagramPacketEncryptedMicData = new DatagramPacket(microphoneDataEncrypted.getData(),
                                                                           microphoneDataBeforeEncryption.getNumBytesRead());

        DatagramPacket decrypt = new DecryptorImpl(key).decrypt(datagramPacketEncryptedMicData);

        Assert.assertNotEquals(microphoneDataBeforeEncryption.getData(), decrypt.getData());
        Assert.assertEquals(new String(dataToEncrypt), new String(decrypt.getData()));
        System.out.println(new String(decrypt.getData()));
    }


    @Test
    public void micTest() throws
                          LineUnavailableException {
        String key = "asdasdada";

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        Microphone mic = new Microphone(format);

        MicrophoneData microphoneData = mic.read();
        DatagramPacket datagramPacket = new DatagramPacket(microphoneData.getData(), microphoneData.getNumBytesRead());

        MicrophoneData encrypt = new EncryptorImpl(key).encrypt(microphoneData);

        Assert.assertNotEquals(new String(encrypt.getData()), new String(datagramPacket.getData()));
    }
}
