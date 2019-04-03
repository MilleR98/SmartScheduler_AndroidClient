package com.example.smartshedulerapp.di_config.module;

import com.example.smartshedulerapp.api.AuthApiService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module(includes = NetModule.class)
public class AuthApiModule {

  @Provides
  @Singleton
  static AuthApiService provideAuthApiService(Retrofit retrofit) {

    return retrofit.create(AuthApiService.class);
  }
}
