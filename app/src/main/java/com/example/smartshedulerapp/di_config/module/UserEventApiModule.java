package com.example.smartshedulerapp.di_config.module;

import com.example.smartshedulerapp.api.EventApiService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module(includes = NetModule.class)
public class UserEventApiModule {

  @Provides
  @Singleton
  static EventApiService provideEventApiService(Retrofit retrofit) {

    return retrofit.create(EventApiService.class);
  }
}
