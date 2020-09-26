package com.rest_providers;

import com.google.common.collect.Lists;
import com.models.CurrentConversationTO;
import com.models.HistoryDisplayData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ConversationProvider {
    final static String url = "127.0.0.1:8080";
    private static RestTemplate restTemplate = RestTemplateConfiguration.restTemplate();

    public static CurrentConversationTO getCurrentConversation(String currentConversationID, String userID, String token) {
        String current = url + "/call/currentConversation";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(current)
                .queryParam("userID", userID)
                .queryParam("conversationID", currentConversationID);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<CurrentConversationTO> entity = new HttpEntity<>(headers);

        return Objects.requireNonNull(
                restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        CurrentConversationTO.class
                ).getBody()
        );
    }

    public static void hangUp(String conversationID, String userID, String token) {
        String current = url + "/call/hangUp";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(current)
                .queryParam("userID", userID)
                .queryParam("conversationID", conversationID);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public static List<HistoryDisplayData> getHistory(String userID, String token) {
        String current = url + "/call/history";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(current)
                .queryParam("userID", userID);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        ArrayList<CurrentConversationTO> currentConversationTOS = Lists.newArrayList(Objects.requireNonNull(restTemplate.exchange(urlBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                CurrentConversationTO[].class)
                .getBody()));

        return currentConversationTOS
                .stream()
                .map(HistoryDisplayData::map)
                .collect(Collectors.toList());
    }
}
