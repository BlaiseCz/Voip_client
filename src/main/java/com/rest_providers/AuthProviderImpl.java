package com.rest_providers;

import com.gui.components.MainController;
import com.models.LoginForm;
import com.models.RegistrationForm;
import com.models.UserTO;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Component
public class AuthProviderImpl implements AuthProvider {
    final String url = "https://voiptipchat.herokuapp.com";
    private static UserTO user;

    @Getter
    private static String token;

    private RestTemplate restTemplate = RestTemplateConfiguration.restTemplate();

    public UserTO login(LoginForm loginForm) {
        String endpointUrl = url + "/auth/login";
        ResponseEntity<UserTO> userTOResponseEntity = restTemplate.postForEntity(endpointUrl, loginForm, UserTO.class);

        user = userTOResponseEntity.getBody();
        token = Objects.requireNonNull(userTOResponseEntity.getHeaders()
                .get("token"))
                .get(0);

        MainController.setUserMe(user.map());
        return userTOResponseEntity.getBody();
    }

    public UserTO register(RegistrationForm registerForm) {
        String endpointUrl = url + "/auth/register";
        System.out.println(registerForm.getIPAddress());
        return restTemplate.postForObject(endpointUrl, registerForm, UserTO.class);
    }

    @Override
    public String renewToken(String actualToken) {
        String endpointUrl = url + "/auth/renewToken";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpointUrl)
                .queryParam("userID", user.getUserID());

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", actualToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.postForEntity(builder.toUriString(), null, Object.class, entity);
        return token;
    }
}
