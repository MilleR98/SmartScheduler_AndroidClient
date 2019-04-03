package com.example.smartshedulerapp.di_config.module;

import com.example.smartshedulerapp.api.NotificationApiService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module(includes = NetModule.class)
public class NotificationApiModule {

  @Provides
  @Singleton
  static NotificationApiService provideNotificationApiService(Retrofit retrofit) {

    return retrofit.create(NotificationApiService.class);
  }
}
