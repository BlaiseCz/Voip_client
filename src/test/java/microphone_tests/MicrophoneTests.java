//package microphone_tests;
//
//import org.junit.Test;
//import com.sound_utils.Microphone;
//
//public class MicrophoneTests {
//
//    @Test
//    public void test() {
//        final Microphone recorder = new Microphone();
//
//        // creates a new thread that waits for a specified
//        // of time before stopping
//        Thread stopper = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                recorder.finish();
//            }
//        });
//
//        stopper.start();
//
//        // start recording
//        recorder.start();
//    }
//}
