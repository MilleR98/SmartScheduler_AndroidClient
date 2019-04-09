package com.example.smartshedulerapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.CreateTaskActivity;
import com.example.smartshedulerapp.adapter.TaskPreviewAdapter;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.TaskPreviewDTO;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

  private static final int REQUEST_TASK_CREATED = 0;

  @Inject
  TaskApiService taskApiService;

  @BindView(R.id.emptyTaskListImage)
  ImageView emptyListBackground;

  @BindView(R.id.taskListRecycleView)
  RecyclerView taskListRecycleView;

  RecyclerView.Adapter adapter;

  public TasksListFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
    ButterKnife.bind(this, view);

    TaskEventComponent taskEventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(getContext())).build();
    taskEventComponent.inject(this);

    taskListRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Loading data...");
    progressDialog.show();

    taskApiService.getUserTasks().enqueue(new Callback<List<TaskPreviewDTO>>() {

      @Override
      public void onResponse(Call<List<TaskPreviewDTO>> call, Response<List<TaskPreviewDTO>> response) {

        progressDialog.dismiss();
        if (response.isSuccessful()) {

          List<TaskPreviewDTO> previewList = response.body();

          if (!previewList.isEmpty()) {

            adapter = new TaskPreviewAdapter(previewList);
            taskListRecycleView.setAdapter(adapter);
            emptyListBackground.setVisibility(View.GONE);
          }
        }

      }

      @Override
      public void onFailure(Call<List<TaskPreviewDTO>> call, Throwable t) {

      }
    });

    return view;
  }

  @OnClick(R.id.createTaskBtn)
  public void openCreateTaskView() {
    Intent intent = new Intent(getContext(), CreateTaskActivity.class);
    startActivityForResult(intent, REQUEST_TASK_CREATED);
  }
}
