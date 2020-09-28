package com.gui.components;

import com.GuiRunner;
import com.models.ConnectionDetails;
import com.models.CurrentConversationTO;
import com.models.HistoryDisplayData;
import com.models.UserShortTO;
import com.rest_providers.*;
import com.runners.ConversationUpdater;
import com.security_utils.DecryptorImpl;
import com.security_utils.EncryptorImpl;
import com.sound_utils.Microphone;
import com.sound_utils.Speaker;
import com.udp_communication.SingleClientVoiceSender;
import com.udp_communication.VoiceReceiver;
import com.udp_communication.VoiceReceiverImpl;
import com.udp_communication.VoiceSender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

@Controller
public class CallPageController {
    private VoiceSender voiceSender;
    private VoiceReceiver voiceReceiver;
    @Getter
    @Setter
    private static boolean visible = false;

    @Setter
    private CurrentConversationTO currentConversation;
    private UserShortTO selectedUser;

    @Autowired
    private MainController mainController;

    @Autowired
    private ConversationUpdater conversationUpdater;

    private AuthProviderImpl authProvider;

    @Autowired
    private UserProviderImpl userProvider;

    @Autowired
    private CallerProvider callerProvider;

    @FXML
    public Button getHistoryBtn;

    @FXML
    public TableColumn<String, HistoryDisplayData> startedHistoryCol;

    @FXML
    private TableView<UserShortTO> usersTable;

    @FXML
    private TableColumn<String, UserShortTO> usernameColumn;

//    @FXML
//    private TableColumn<Booleashn, UserShortTO> favouriteColumn;

    @FXML
    private TableColumn<Boolean, UserShortTO> statusColumn;
    //    @FXML
//    public TableColumn<String, HistoryDisplayData> endedHistoryCol;
    @FXML
    public TableColumn<String, HistoryDisplayData> participantsHistoryCol;
    @FXML
    private TableView<HistoryDisplayData> historyTable;
    @FXML
    private CheckBox favouriteCheckBox;

    @FXML
    private Button disconnectBtn;

    @FXML
    private Button muteBtn;

    @FXML
    private Button callBtn;

    @FXML
    private Label selectedUserLbl;

    @FXML
    private Label userIpLbl;

    @FXML
    void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory("username"));
        statusColumn.setCellValueFactory(new PropertyValueFactory("active"));
//        favouriteColumn.setCellValueFactory(new PropertyValueFactory("favourite"));

        initRefreshingUsersThread();
        //muteBtn.setDisable(true);
        //disconnectBtn.setDisable(true);

        new Thread(() -> {
            while(GuiRunner.isRunning()) {
                try {
                    Thread.sleep(10000);
                    initHistory();
                    refreshHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initHistory() {
//        endedHistoryCol.setCellValueFactory(new PropertyValueFactory<>("ended"));
        startedHistoryCol.setCellValueFactory(new PropertyValueFactory<>("began"));
        participantsHistoryCol.setCellValueFactory(new PropertyValueFactory<>("participants"));
    }

    public void refreshHistory() {
        ObservableList<HistoryDisplayData> history = FXCollections.observableList(
                ConversationProvider.getHistory(
                        MainController.getUserMe().getUserID(),
                        mainController.getTokenService().getToken())
        );

        historyTable.setItems(history);
    }

    @FXML
    void updateSelectedUser() {
        selectedUser = usersTable.getSelectionModel()
                .getSelectedItem();

//        favouriteCheckBox.setSelected(selectedUser.isFavourite());
        updateSelectedUserInfo();
    }

    @FXML
    void call(ActionEvent event) throws
                                 IOException,
                                 LineUnavailableException {
/*        muteBtn.setDisable(false);
        disconnectBtn.setDisable(false);
        callBtn.setDisable(true);*/


        try {
            String[] conversationID = new String[1];
            startCall(selectedUser, conversationID, true);

            callerProvider.callTo(MainController.getUserMe(), selectedUser, conversationID[0]);

        } catch (Exception e) {
            this.disconnect(null);
        }
    }

    @FXML
    void mute(ActionEvent event) {
        if (muteBtn.getText()
                   .equalsIgnoreCase("mute")) {
            muteBtn.setText("Unmute");
            voiceSender.pauseSending();
        } else {
            muteBtn.setText("Mute");
            voiceSender.resumeSending();
        }
    }

    @FXML
    void disconnect(ActionEvent event) {
        try {
            voiceSender.stopSending();
        } catch (Exception ignored) {
        }

        conversationUpdater.stopUpdatingAndHangUp(mainController.getTokenService().getToken());
        voiceReceiver.stopListening();
/*        callBtn.setDisable(false);
        disconnectBtn.setDisable(true);
        muteBtn.setDisable(true);*/

        refreshHistory();
    }

    @FXML
    void favouriteChanged(ActionEvent event) {
        if (favouriteCheckBox.isSelected()) {
            userProvider.addToFavourites(selectedUser.getUsername(), mainController.getTokenService().getToken());
        } else {
            System.out.println("Deleted");
            userProvider.deleteFromFavourites(selectedUser.getUsername(), mainController.getTokenService().getToken());
        }
    }

    public void informAboutNewCall(UserShortTO callingUser, String[] conversationID) {
        Platform.runLater(() -> {
            Optional<ButtonType> result = AlertController.showCallAlert(callingUser);

            if (result.isPresent() && result.get()
                    .equals(ButtonType.OK)) {
                try {
                    startCall(callingUser, conversationID, false);
                } catch (IOException | LineUnavailableException e) {
                    disconnect(null);
                    e.printStackTrace();
                }
            }
        });
    }

    private void informServerAboutHangUp() {

    }

    private void initRefreshingUsersThread() {
        new Thread(() -> {
            while (GuiRunner.isRunning()) {
                try {
                    if (CallPageController.isVisible()) {
                        List<UserShortTO> allUsers = userProvider.getFavourites(mainController.getTokenService()
                                .getToken());
                        usersTable.setItems(FXCollections.observableArrayList(allUsers));
                        Thread.sleep(2000);
                    }
                    else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateSelectedUserInfo() {
        selectedUserLbl.setText(selectedUser.getUsername());
        userIpLbl.setText(selectedUser.getIPAddress());
    }

    private void startCall(UserShortTO user, String[] conversationID, boolean calling) throws
            IOException,
            LineUnavailableException {

        String token = mainController.getTokenService().getToken();
        String keyForConversation = DHProvider.getKeyForConversation(calling, token, conversationID);
        disconnectBtn.setDisable(false);
        muteBtn.setDisable(false);

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        ConnectionDetails receiverConnection = new ConnectionDetails(InetAddress.getByName("localhost"), 5555, 1024);
        ConnectionDetails senderConnection = new ConnectionDetails(InetAddress.getByName(user.getIPAddress()),
                                                                   5555,
                                                                   1024);

        Speaker speaker = new Speaker(format);
        voiceReceiver = new VoiceReceiverImpl(receiverConnection, speaker, new DecryptorImpl(keyForConversation));
        voiceReceiver.startListening();

        try {
            Microphone microphone = new Microphone(format);
            voiceSender = new SingleClientVoiceSender(senderConnection, microphone, new EncryptorImpl(keyForConversation));
            voiceSender.startSending();
        } catch (Exception e) {
            AlertController.showAlert("You have no microphone!", null, "Check your microphone settings!");
        }

        conversationUpdater.startUpdating(this::setCurrentConversation, conversationID[0], MainController.getUserMe().getUserID(), token);
    }
}
