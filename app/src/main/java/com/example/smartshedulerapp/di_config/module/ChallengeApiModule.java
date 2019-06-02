package com.example.smartshedulerapp.di_config.module;

import com.example.smartshedulerapp.api.ChallengeApiService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module(includes = NetModule.class)
public class ChallengeApiModule {

  @Provides
  @Singleton
  static ChallengeApiService provideChellengeApiService(Retrofit retrofit) {

    return retrofit.create(ChallengeApiService.class);
  }
}
