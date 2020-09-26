package com.rest_providers;

import com.gui.components.MainController;
import com.models.ConversationTO;
import com.models.DHRequestTO;
import com.models.UserShortTO;
import com.models.UserTO;
import com.security_utils.DHUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Component
public class DHProvider {
    final static String url = "127.0.0.1:8080";
    private static RestTemplate restTemplate = RestTemplateConfiguration.restTemplate();

    public static String getKeyForConversation(
            boolean startingConversation,
            String token,
            String[] conversationID
    ) {
        if (startingConversation) {
            String calling = url + "/call/calling";
            DHRequestTO dhRequestStart = DHUtils.generateDHRequest();

            ConversationTO response = requestWhile(calling, dhRequestStart, token);

            conversationID[0] = response.getID();
            return DHUtils.getKeyFromDHResponse(response);
        } else {
            String beingCalled = url + "/call/beingCalled";
            DHRequestTO dhRequestBeingCalled = DHUtils.generateDHRequest(conversationID[0]);

            ConversationTO response = requestWhile(beingCalled, dhRequestBeingCalled, token);

            return DHUtils.getKeyFromDHResponse(response);
        }
    }

    private static ConversationTO requestWhile(String what, DHRequestTO dhRequestTO, String token) {
        UserShortTO userMe = MainController.getUserMe();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(what)
                .queryParam("userID", userMe.getUserID());
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<DHRequestTO> entity = new HttpEntity<>(dhRequestTO, headers);

        return Objects.requireNonNull(
                restTemplate.postForEntity(builder.toUriString(), entity, ConversationTO.class).getBody()
        );
    }
}
