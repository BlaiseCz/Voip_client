package com.gui.components;

import com.models.LoginForm;
import com.rest_providers.AuthProviderImpl;
import com.utils.IpUtils;
import com.utils.PasswordUtils;
import com.utils.PreferencesKeys;
import com.utils.PreferencesUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private MainController mainController;

    @Autowired
    private AuthProviderImpl userProvider;

    @FXML
    private Button registerBtn;

    @FXML
    @Getter
    private TextField usernameTF;

    @FXML
    private Button loginBtn;

    @FXML
    @Getter
    private CheckBox rememberMeCB;

    @FXML
    @Getter
    private PasswordField passwordTF;

    @FXML
    private AnchorPane mainPane;

    @FXML
    void login(ActionEvent event) throws
                                  IOException {
        LoginForm loginForm = LoginForm
                .builder()
                .username(usernameTF.getText())
                .password(PasswordUtils.getPasswordHash(passwordTF.getText()))
                .IPAddress(IpUtils.getLocalIpAddr())
                .build();

        login(loginForm);
    }

    @FXML
    void register(ActionEvent event) {
        mainController.switchToRegister();
    }

    void login(LoginForm loginForm) {
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    userProvider.login(loginForm);
                    mainController.initRefreshingToken(AuthProviderImpl.getToken());
                    mainController.switchToCall();

                    if (PreferencesUtils.getValue(PreferencesKeys.REMEMBER_ME)) {
                        // do nothing
                    } else if (rememberMeCB.isSelected()) {
                        PreferencesUtils.setValue(PreferencesKeys.REMEMBER_ME, true);
                        PreferencesUtils.setValue(PreferencesKeys.USERNAME, loginForm.getUsername());
                        PreferencesUtils.setValue(PreferencesKeys.PASSWORD_HASH, loginForm.getPassword());
                    } else {
                        PreferencesUtils.setValue(PreferencesKeys.REMEMBER_ME, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertController.showAlert("Failed to login!",
                                              null,
                                              "Register or activate your account!");
                }
            });

        }).start();
    }
}

