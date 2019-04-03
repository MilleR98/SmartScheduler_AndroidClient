package com.example.smartshedulerapp.di_config.module;

import com.example.smartshedulerapp.api.SubtaskApiService;
import com.example.smartshedulerapp.api.TaskApiService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module(includes = NetModule.class)
public class UserTaskApiModule {

  @Provides
  @Singleton
  static TaskApiService provideTaskApiService(Retrofit retrofit) {

    return retrofit.create(TaskApiService.class);
  }

  @Provides
  @Singleton
  static SubtaskApiService provideSubtaskApiService(Retrofit retrofit) {

    return retrofit.create(SubtaskApiService.class);
  }
}
