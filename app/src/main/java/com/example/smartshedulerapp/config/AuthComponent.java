package com.example.smartshedulerapp.config;

import com.example.smartshedulerapp.activity.SignInActivity;
import com.example.smartshedulerapp.activity.SignUpActivity;
import com.example.smartshedulerapp.api.AuthApiService;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = AuthApiModule.class)
public interface AuthComponent {

  AuthApiService getAuthApiService();

  void inject(SignInActivity signInActivity);

  void inject(SignUpActivity signUpActivity);
}
