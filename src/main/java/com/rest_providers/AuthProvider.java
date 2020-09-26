package com.rest_providers;

import com.models.LoginForm;
import com.models.RegistrationForm;
import com.models.UserTO;

public interface AuthProvider {
    UserTO login(LoginForm loginForm);

    UserTO register(RegistrationForm registerForm);

    String renewToken(String actualToken);
}
