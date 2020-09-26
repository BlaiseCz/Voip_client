/********************************************
 *
 * Copyright (c) 2003-2018 XML-INTL Ltd.
 *
 * All Rights Reserved
 *
 ********************************************/
package com.gui.components;

import com.models.UserShortTO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;


public class AlertController {
    /**
     * This is static class. This constructor is
     * not to have constructor visible while
     * developing features.
     */
    private AlertController() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Shows dialog pop-up.
     */
    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Allows to get from dialog input.
     */
    public static String getValueFromDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public static Optional<ButtonType> showCallAlert(UserShortTO callingUser) {
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "User " + callingUser.getUsername() + " is calling.\nWould you like to answer?",
                ButtonType.CANCEL, ButtonType.OK);

        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }
}
