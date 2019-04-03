package com.example.smartshedulerapp.api;

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

public interface SubtaskApiService {

  @GET("/subtask")
  Call<List<Subtask>> getTaskSubtasks(@Query("taskId") String taskId);

  @GET("/subtask/{id}")
  Call<Subtask> getSubtasksInfo(@Path("id") String id);

  @POST("/subtask")
  Call<ResponseBody> createTask(@Body Subtask simpleTask);

  @PUT("/subtask/{id}")
  Call<ResponseBody> updateSubtask(@Path("id") String id, @Body Subtask subtask);

  @DELETE("/subtask/{id}")
  Call<ResponseBody> removeSubtask(@Path("id") String id);
}
