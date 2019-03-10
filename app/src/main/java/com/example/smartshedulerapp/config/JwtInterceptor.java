package com.example.smartshedulerapp.config;

import static com.example.smartshedulerapp.util.Constants.AUTH_TOKEN_KEY;

import android.content.Context;
import android.preference.PreferenceManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";
  private Context context;

  public JwtInterceptor(Context context) {
    this.context = context;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {

    String authToken = PreferenceManager.getDefaultSharedPreferences(context).getString(AUTH_TOKEN_KEY, "");

    Request request = chain.request();

    request = request.newBuilder()
        .addHeader(AUTHORIZATION_HEADER, AUTH_HEADER_VALUE_PREFIX + authToken)
        .build();

    return chain.proceed(request);
  }
}
