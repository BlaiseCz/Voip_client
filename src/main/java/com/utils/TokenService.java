package com.utils;

import com.rest_providers.AuthProviderImpl;
import lombok.Getter;

public class TokenService {
    @Getter
    private String token;

    private AuthProviderImpl authProvider = new AuthProviderImpl();

    public TokenService(String token) {
        this.token = token;

        new Thread(() -> {
            while (true) {
                try {
                    this.token = authProvider.renewToken(this.token);
                    Thread.sleep(10000);
                } catch (Exception e) {

                }
            }
        }).start();
    }
}
