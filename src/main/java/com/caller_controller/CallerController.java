package com.caller_controller;

import com.gui.components.CallPageController;
import com.models.MessageTO;
import com.models.UserShortTO;
import com.models.UserTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
public class CallerController {

    @Autowired
    private CallPageController controller;

    @PostMapping("/call")
    public MessageTO serveIncomingCall(@RequestBody UserShortTO user, @RequestParam String conversationID) {
        String[] conversationIDContainer = {conversationID};
        controller.informAboutNewCall(user, conversationIDContainer);
        return new MessageTO("User informed!");
    }
}
