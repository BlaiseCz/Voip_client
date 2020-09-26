package com.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDAO {
    private int _id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;

    @Builder.Default
    private boolean isEmailValidated = false;
    @Builder.Default
    private List<UserShortDAO> favourites = new ArrayList<>();
    private String IPAddress;
}
