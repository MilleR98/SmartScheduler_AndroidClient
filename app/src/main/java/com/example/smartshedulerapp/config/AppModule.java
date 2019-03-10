package com.example.smartshedulerapp.config;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {

  private Context context;

  public AppModule(Context context) {
    this.context = context;
  }

  @Provides
  @Singleton
  public Context provideContext(){

    return context;
  }
}
