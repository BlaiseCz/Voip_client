package com.gui.components;

import com.models.LoginForm;
import com.models.UserShortTO;
import com.models.UserTO;
import com.utils.IpUtils;
import com.utils.PreferencesKeys;
import com.utils.PreferencesUtils;
import com.utils.TokenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    @Getter
    @Setter
    private static UserShortTO userMe;

    @Getter
    private TokenService tokenService;

    @FXML
    private VBox callPane;

    @FXML
    private VBox registerPane;

    @FXML
    private VBox loginPane;

    @FXML
    private LoginController loginController;

    @FXML
    private RegisterController registerController;

    @FXML
    private CallPageController callPageController;

    @FXML
    void initialize() throws
                      Exception {
        switchToLogin();

        if (PreferencesUtils.getValue(PreferencesKeys.REMEMBER_ME)) {
            String passwordHash = PreferencesUtils.getValue(PreferencesKeys.PASSWORD_HASH);
            String username = PreferencesUtils.getValue(PreferencesKeys.USERNAME);

            LoginForm loginForm = LoginForm
                    .builder()
                    .username(username)
                    .password(passwordHash)
                    .IPAddress(IpUtils.getLocalIpAddr())
                    .build();

            loginController.login(loginForm);
        }
    }

    @FXML
    void chooseServer(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) throws
                                   Exception {
        PreferencesUtils.setValue(PreferencesKeys.REMEMBER_ME, false);
        loginController.getPasswordTF()
                       .clear();
        loginController.getRememberMeCB()
                       .setSelected(false);
        loginController.getUsernameTF()
                       .clear();
        switchToLogin();
    }

    @FXML
    void closeApp(ActionEvent event) {
        System.exit(0);
    }

    public void switchToLogin() {
        makePanelsVisible(false, true, false);
    }

    public void switchToRegister() {
        makePanelsVisible(false, false, true);
    }

    public void switchToCall() {
        makePanelsVisible(true, false, false);
    }

    private void makePanelsVisible(boolean callPage, boolean loginPage, boolean registerPage) {
        CallPageController.setVisible(callPage);
        callPane.setVisible(callPage);
        loginPane.setVisible(loginPage);
        registerPane.setVisible(registerPage);
    }

    public void initRefreshingToken(String token) {
        tokenService = new TokenService(token);
    }
}
