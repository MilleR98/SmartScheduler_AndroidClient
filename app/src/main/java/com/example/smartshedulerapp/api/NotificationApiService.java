package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.Notification;
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

public interface NotificationApiService {

  @GET("/notifications")
  Call<List<Notification>> getUserNotifications(@Query("userId") String userId);

  @GET("/notifications/{id}")
  Call<Notification> getNotification(@Path("id") String id);

  @POST("/notifications")
  Call<ResponseBody> createNotifications(@Body Notification notification);

  @PUT("/notifications/{id}")
  Call<ResponseBody> updateNotification(@Path("id") String id, @Body Notification notification);

  @DELETE("/notifications/{id}")
  Call<ResponseBody> removeNotification(@Path("id") String id);
}
