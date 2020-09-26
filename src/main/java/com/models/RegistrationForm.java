package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class RegistrationForm {
    private String username;
    private String email;
    private String password;
    @JsonProperty("IPAddress")
    private String IPAddress;
}
