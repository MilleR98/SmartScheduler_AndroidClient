package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.Challenge;
import com.example.smartshedulerapp.model.Subtask;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChallengeApiService {

  @GET("/challenges")
  Call<List<Challenge>> getUserChallenges();

  @POST("/challenges")
  Call<ResponseBody> createChallenge(@Body Challenge challenge);

  @DELETE("/challenges/{id}")
  Call<ResponseBody> removeChallenge(@Path("id") String id);
}
