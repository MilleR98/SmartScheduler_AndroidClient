package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.SignInDTO;
import com.example.smartshedulerapp.model.SignUpDTO;
import com.example.smartshedulerapp.model.UserDetails;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

  @POST("/auth/sign-in")
  Call<UserDetails> signIn(@Body SignInDTO signInDTO);

  @POST("/auth/sign-up")
  Call<ResponseBody> signUp(@Body SignUpDTO signUpDTO);
}
