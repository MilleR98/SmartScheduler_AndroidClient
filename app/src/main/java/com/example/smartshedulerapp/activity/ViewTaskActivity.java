package com.example.smartshedulerapp.activity;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.adapter.SubtaskAdapter;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.dialog.CreateSubtaskDialog;
import com.example.smartshedulerapp.model.Subtask;
import com.example.smartshedulerapp.model.TaskInfoDTO;
import com.example.smartshedulerapp.model.type.ReminderType;
import com.example.smartshedulerapp.model.type.SubtaskPriority;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTaskActivity extends AppCompatActivity implements CreateSubtaskDialog.CreateSubtaskDialogListener {

  @Inject
  TaskApiService taskApiService;

  @BindView(R.id.subtaskListView)
  ListView subtaskListView;
  @BindView(R.id.taskTitleLabel)
  TextView taskTitleLabel;
  @BindView(R.id.taskDeadline)
  TextView taskDeadlineLabel;
  @BindView(R.id.taskReminderType)
  TextView taskReminderType;
  @BindView(R.id.taskReminderTime)
  TextView taskReminderTime;
  @BindView(R.id.taskDescription)
  TextView taskDescription;
  List<Subtask> subtaskList = new ArrayList<>();
  private TaskInfoDTO currentTaskInfo;
  private SubtaskAdapter subtaskAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_task);

    ButterKnife.bind(this);

    TaskEventComponent eventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    eventComponent.inject(this);

    String taskId = getIntent().getStringExtra("taskId");

    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading task info...");
    progressDialog.show();

    subtaskAdapter = new SubtaskAdapter(getApplicationContext());
    subtaskAdapter.setItemModelList(subtaskList);
    subtaskListView.setAdapter(subtaskAdapter);

    fetchCurrentTaskInfo(taskId, progressDialog);
  }

  @OnClick(R.id.backToTaskList)
  public void onBackToTaskss() {
    finish();
  }

  @OnClick(R.id.imgViewAdd)
  public void addValue() {
    openCreateSubtaskDialog();
  }

  public void openCreateSubtaskDialog() {
    CreateSubtaskDialog exampleDialog = new CreateSubtaskDialog();
    exampleDialog.show(getSupportFragmentManager(), "Create subtask dialog");
  }

  public void editTask() {

    Intent intent = new Intent(this, CreateTaskActivity.class);
    intent.putExtra("currentTask", currentTaskInfo);
    intent.putExtra("forEdit", true);

    startActivity(intent);
  }

  public void deleteTask() {

    taskApiService.removeTask(currentTaskInfo.getId()).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        Toast.makeText(getApplicationContext(), "Task " + currentTaskInfo.getTitle() + " removed", Toast.LENGTH_LONG).show();
        finish();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Toast.makeText(getApplicationContext(), "Fail with removing task", Toast.LENGTH_LONG).show();
      }
    });
  }

  @OnClick(R.id.taskActionsImg)
  public void showPopup(View v) {
    PopupMenu popup = new PopupMenu(this, v);
    MenuInflater inflater = popup.getMenuInflater();
    inflater.inflate(R.menu.task_actions, popup.getMenu());

    popup.setOnMenuItemClickListener(item -> {

      boolean result = true;

      if (item.getItemId() == R.id.editTask) {

        editTask();
      } else if (item.getItemId() == R.id.deleteTask) {

        new AlertDialog.Builder(this)
            .setTitle("Remove confirmation")
            .setMessage("Do you really want to delete task")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteTask())
            .setNegativeButton(android.R.string.no, null)
            .show();

      }else {

        result =  super.onOptionsItemSelected(item);
      }

      return result;
    });

    popup.show();
  }

  private void fetchCurrentTaskInfo(String taskId, ProgressDialog progressDialog) {
    taskApiService.getTaskInfo(taskId).enqueue(new Callback<TaskInfoDTO>() {

      @Override
      public void onResponse(Call<TaskInfoDTO> call, Response<TaskInfoDTO> response) {

        progressDialog.dismiss();

        if (response.isSuccessful()) {

          currentTaskInfo = response.body();

          taskTitleLabel.setText(currentTaskInfo.getTitle());
          taskDeadlineLabel.setText(currentTaskInfo.getDeadlineDate().format(DATE_TIME_FORMATTER));
          taskDescription.setText(currentTaskInfo.getDescription());
          taskReminderType.setText(currentTaskInfo.getReminderType().name());

          String reminderTime = currentTaskInfo.getReminderType().equals(ReminderType.ONE_TIME) ?
              currentTaskInfo.getReminderTime().format(DATE_TIME_FORMATTER) :
              currentTaskInfo.getReminderTime().toLocalTime().toString();

          taskReminderTime.setText(reminderTime);

          subtaskList = currentTaskInfo.getSubtaskList();
          subtaskAdapter.setItemModelList(subtaskList);
          subtaskAdapter.notifyDataSetChanged();
        } else {

          Toast.makeText(getApplicationContext(), "Fail with loading task info", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<TaskInfoDTO> call, Throwable t) {

        Toast.makeText(getApplicationContext(), "Fail with loading task info", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
      }
    });
  }

  @Override
  public void applyValues(String subtaskName, SubtaskPriority subtaskPriority) {

    Subtask newSubtask = new Subtask();
    newSubtask.setName(subtaskName);
    newSubtask.setTaskId(currentTaskInfo.getId());
    newSubtask.setPriority(subtaskPriority);

    subtaskList.add(newSubtask);
    subtaskAdapter.notifyDataSetChanged();

    taskApiService.createSubtask(newSubtask).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {

          Toast.makeText(getApplicationContext(), "New subtask created", Toast.LENGTH_LONG).show();
        } else {

          Toast.makeText(getApplicationContext(), "Fail create subtask", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Toast.makeText(getApplicationContext(), "Fail create subtask", Toast.LENGTH_LONG).show();
      }
    });
  }
}
