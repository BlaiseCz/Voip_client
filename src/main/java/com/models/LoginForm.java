package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginForm {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    @JsonProperty("IPAddress")
    private String IPAddress;
}

