package com.rest_providers;

import com.models.MessageTO;
import com.models.UserShortTO;
import com.models.UserTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CallerProvider {
    private String port = ":8080/internal";

    private RestTemplate restTemplate = RestTemplateConfiguration.restTemplate();

    public MessageTO callTo(UserShortTO userMe, UserShortTO callingUser, String conversationID) {
        String url = "http://" + callingUser.getIPAddress() + port + "/call";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("conversationID", conversationID);
        return restTemplate.postForObject(builder.toUriString(), userMe, MessageTO.class);
    }
}
