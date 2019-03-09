package com.example.smartshedulerapp.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

  @Provides
  @Singleton
  static Gson provideGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();

    return gsonBuilder.create();
  }

  @Provides
  @Singleton
  static OkHttpClient provideOkhttpClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.addInterceptor(logging);
    httpClient.connectTimeout(30, TimeUnit.SECONDS);
    httpClient.readTimeout(30, TimeUnit.SECONDS);

    return httpClient.build();
  }

  @Provides
  @Singleton
  static Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl("https://d82cb2ee.ngrok.io")
        .client(okHttpClient)
        .build();
  }
}
