package com.runners;

import com.models.CurrentConversationTO;
import com.rest_providers.ConversationProvider;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ConversationUpdater {
    private String conversationID;
    private String userID;

    @Setter
    private boolean shouldRun;

    public void startUpdating(Consumer<CurrentConversationTO> consumer, String conversationID, String userID, String token) {
        shouldRun = true;
        this.conversationID = conversationID;
        this.userID = userID;

        new Thread(() -> {
            while (shouldRun) {
                CurrentConversationTO currentConversation = ConversationProvider.getCurrentConversation(conversationID, userID, token);
                consumer.accept(currentConversation);
            }
            try {
                wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopUpdatingAndHangUp(String token) {
        shouldRun = false;
        ConversationProvider.hangUp(conversationID, userID, token);
    }
}
