package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserShortTO {
    private String userID;
    private String username;
    @JsonProperty("ipaddress")
    private String IPAddress;
    private boolean favourite = false;
    private boolean active = false;
}