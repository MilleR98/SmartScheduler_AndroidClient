package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.activity.SignInActivity;
import com.example.smartshedulerapp.activity.SignUpActivity;
import com.example.smartshedulerapp.api.AuthApiService;
import com.example.smartshedulerapp.di_config.module.AuthApiModule;
import com.example.smartshedulerapp.fragment.ProfileFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = AuthApiModule.class)
public interface AuthComponent {

  AuthApiService getAuthApiService();

  void inject(SignInActivity signInActivity);

  void inject(SignUpActivity signUpActivity);

  void inject(ProfileFragment fragment);
}
