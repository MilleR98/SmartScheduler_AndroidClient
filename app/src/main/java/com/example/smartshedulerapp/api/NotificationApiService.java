package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.Notification;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NotificationApiService {

  @GET("/notifications")
  Call<List<Notification>> getUserNotifications();

  @DELETE("/notifications/{id}")
  Call<ResponseBody> removeNotification(@Path("id") String id);

  @POST("/events/{id}/invitation/decline")
  Call<ResponseBody> declineInvitation(@Path("id") String eventId);

  @POST("/events/{id}/invitation/accept")
  Call<ResponseBody> acceptInvitation(@Path("id") String eventId);
}
