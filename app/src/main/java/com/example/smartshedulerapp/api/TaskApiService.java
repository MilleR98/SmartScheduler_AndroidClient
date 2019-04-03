package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.SimpleTask;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskApiService {

  @GET("/tasks")
  Call<List<SimpleTask>> getUserTasks();

  @GET("/tasks/{id}")
  Call<SimpleTask> getTaskInfo(@Path("id") String id);

  @POST("/tasks")
  Call<ResponseBody> createTask(@Body SimpleTask simpleTask);

  @PUT("/tasks/{id}")
  Call<ResponseBody> updateTask(@Path("id") String id, @Body SimpleTask simpleTask);

  @DELETE("/tasks/{id}")
  Call<ResponseBody> removeTask(@Path("id") String id);
}
