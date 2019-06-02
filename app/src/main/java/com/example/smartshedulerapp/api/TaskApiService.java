package com.example.smartshedulerapp.api;

import com.example.smartshedulerapp.model.CreateTaskDTO;
import com.example.smartshedulerapp.model.Subtask;
import com.example.smartshedulerapp.model.TaskInfoDTO;
import com.example.smartshedulerapp.model.TaskPreviewDTO;
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

  @GET("/tasks/previews")
  Call<List<TaskPreviewDTO>> getUserTasks();

  @GET("/tasks/{id}")
  Call<TaskInfoDTO> getTaskInfo(@Path("id") String id);

  @POST("/tasks")
  Call<ResponseBody> createTask(@Body CreateTaskDTO simpleTask);

  @POST("/subtasks")
  Call<ResponseBody> createSubtask(@Body Subtask subtask);

  @DELETE("/subtasks/{id}")
  Call<ResponseBody> deleterSubtask(@Path("id") String id);

  @PUT("/tasks/{id}")
  Call<ResponseBody> updateTask(@Path("id") String id, @Body CreateTaskDTO createTaskDTO);

  @DELETE("/tasks/{id}")
  Call<ResponseBody> removeTask(@Path("id") String id);
}
