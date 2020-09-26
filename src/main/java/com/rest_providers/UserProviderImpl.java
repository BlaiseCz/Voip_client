package com.rest_providers;

import com.google.common.collect.Lists;
import com.gui.components.MainController;
import com.models.UserShortTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class UserProviderImpl {
    final String url = "127.0.0.1:8080/users";
    private final RestTemplate restTemplate = RestTemplateConfiguration.restTemplate();

    public List<UserShortTO> getFavourites(String token) {
        String endpointUrl = url + "/favourites";

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
                .queryParam("userID",
                        MainController.getUserMe()
                                .getUserID());

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<UserShortTO> users = Lists.newArrayList(restTemplate.exchange(urlBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                UserShortTO[].class)
                .getBody());

        return users;
    }

    public void addToFavourites(String favUsername, String token) {
        String endpointUrl = url + "/favourites";

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
                .queryParam("userID",
                        MainController.getUserMe()
                                .getUserID())
                .queryParam("favUsername", favUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(urlBuilder.toUriString(),
                HttpMethod.POST,
                entity,
                Object.class);
    }

    public void deleteFromFavourites(String favUsername, String token) {
        String endpointUrl = url + "/favourites";

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
                .queryParam("userID",
                        MainController.getUserMe()
                                .getUserID())
                .queryParam("favUsername", favUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange(urlBuilder.toUriString(),
                HttpMethod.DELETE,
                entity,
                Object.class);
    }
}
