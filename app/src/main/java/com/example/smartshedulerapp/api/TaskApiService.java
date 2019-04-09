package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.CreateTaskDTO;
import com.example.smartshedulerapp.model.TaskPreviewDTO;
import com.google.android.gms.tasks.Task;
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
  Call<List<TaskPreviewDTO>> getUserTasks();

  @GET("/tasks/{id}")
  Call<Task> getTaskInfo(@Path("id") String id);

  @POST("/tasks")
  Call<ResponseBody> createTask(@Body CreateTaskDTO simpleTask);

  @PUT("/tasks/{id}")
  Call<ResponseBody> updateTask(@Path("id") String id, @Body CreateTaskDTO createTaskDTO);

  @DELETE("/tasks/{id}")
  Call<ResponseBody> removeTask(@Path("id") String id);
}
