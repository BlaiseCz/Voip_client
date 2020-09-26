package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserTO {
    private String userID;

    private boolean favourite = false;

    private String username;
    private String email;
    @JsonProperty("ipaddress")
    private String IPAddress;

    public UserShortTO map() {
        return UserShortTO.builder()
                .userID(getUserID())
                .username(getUsername())
                .favourite(isFavourite())
                .IPAddress(getIPAddress())
//                .active(false)
                     .build();
    }
}
