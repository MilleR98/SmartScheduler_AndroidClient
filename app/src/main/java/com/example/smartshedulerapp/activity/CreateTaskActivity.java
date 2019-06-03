package com.example.smartshedulerapp.activity;

import static android.content.ContentValues.TAG;
import static com.example.smartshedulerapp.model.type.ReminderType.DAILY;
import static com.example.smartshedulerapp.model.type.ReminderType.MONTHLY;
import static com.example.smartshedulerapp.model.type.ReminderType.ONE_TIME;
import static com.example.smartshedulerapp.model.type.ReminderType.WEEKLY;
import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.smartshedulerapp.model.CreateTaskDTO;
import com.example.smartshedulerapp.model.Subtask;
import com.example.smartshedulerapp.model.TaskInfoDTO;
import com.example.smartshedulerapp.model.type.ReminderType;
import com.example.smartshedulerapp.model.type.SubtaskPriority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    CreateSubtaskDialog.CreateSubtaskDialogListener {

  @Inject
  TaskApiService taskApiService;
  boolean isDeadlineSelecting;
  @BindView(R.id.create_task_title)
  TextView topBarLabel;
  @BindView(R.id.inputTaskTitle)
  EditText inputTaskTitle;
  @BindView(R.id.inputTaskDescription)
  EditText inputTaskDescription;
  @BindView(R.id.inputDeadlineDate)
  EditText inputDeadlineDate;
  @BindView(R.id.reminderTime)
  EditText reminderTime;
  @BindView(R.id.subtaskListView)
  ListView subtaskListView;
  @BindView(R.id.reminderTypeGroup)
  RadioGroup reminderTypeGroup;
  List<Subtask> subtaskList = new ArrayList<>();
  SubtaskAdapter subtaskAdapter;
  private LocalDateTime selectedDeadlineDate;
  private LocalDateTime selectedReminderDate;
  private boolean isForUpdate;
  private CreateTaskDTO createTaskDTO;
  private String taskId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_task);

    ButterKnife.bind(this);

    TaskEventComponent authComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    authComponent.inject(this);

    subtaskAdapter = new SubtaskAdapter(getApplicationContext());
    subtaskAdapter.setItemModelList(subtaskList);
    subtaskListView.setAdapter(subtaskAdapter);

    isForUpdate = getIntent().getBooleanExtra("forEdit", false);

    if (isForUpdate) {

      TaskInfoDTO taskInfoDTO = (TaskInfoDTO) getIntent().getSerializableExtra("currentTask");
      taskId = taskInfoDTO.getId();
      inputTaskTitle.setText(taskInfoDTO.getTitle());
      createTaskDTO.setTitle(taskInfoDTO.getTitle());
      inputTaskDescription.setText(taskInfoDTO.getDescription());
      createTaskDTO.setDescription(taskInfoDTO.getDescription());
      inputDeadlineDate.setText(taskInfoDTO.getDeadlineDate().format(DATE_TIME_FORMATTER));
      createTaskDTO.setDescription(taskInfoDTO.getDescription());
      reminderTime.setText(taskInfoDTO.getReminderTime().format(DATE_TIME_FORMATTER));
      createTaskDTO.setReminderTime(taskInfoDTO.getReminderTime());

      switch (taskInfoDTO.getReminderType()) {
        case DAILY:
          reminderTypeGroup.check(R.id.dailyNotification);
          break;
        case ONE_TIME:
          reminderTypeGroup.check(R.id.oneTimeNotification);
          break;
        case WEEKLY:
          reminderTypeGroup.check(R.id.weeklyNotification);
          break;
        case MONTHLY:
          reminderTypeGroup.check(R.id.monthlyNotification);
          break;
      }

      createTaskDTO.setReminderType(taskInfoDTO.getReminderType());

      topBarLabel.setText("Edit task");
    } else {

      topBarLabel.setText("Create task");
      createTaskDTO = new CreateTaskDTO();
    }
  }

  @OnClick(R.id.imgViewAdd)
  public void addValue() {
    openCreateSubtaskDialog();
  }

  public void openCreateSubtaskDialog() {
    CreateSubtaskDialog exampleDialog = new CreateSubtaskDialog();
    exampleDialog.show(getSupportFragmentManager(), "Create subtask dialog");
  }

  @OnClick(R.id.inputDeadlineDate)
  public void inputDateClick() {
    isDeadlineSelecting = true;

    openDatePickerDialog();
  }

  @OnClick(R.id.reminderTime)
  public void inputReminderTimeClick() {
    isDeadlineSelecting = false;

    openDatePickerDialog();
  }

  private void openDatePickerDialog() {
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);

    DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, this, year, month, day);
    datePickerDialog.show();
  }

  @OnClick(R.id.submitCreateTask)
  public void createTaskSubmit() {

    int checkedRadioButtonId = reminderTypeGroup.getCheckedRadioButtonId();

    ReminderType reminderType;

    if (checkedRadioButtonId == R.id.dailyNotification) {

      reminderType = DAILY;
    } else if (checkedRadioButtonId == R.id.weeklyNotification) {

      reminderType = WEEKLY;
    } else if (checkedRadioButtonId == R.id.monthlyNotification) {

      reminderType = MONTHLY;
    } else {

      reminderType = ONE_TIME;
    }

    createTaskDTO.setDeadlineDate(selectedDeadlineDate);
    createTaskDTO.setReminderTime(selectedReminderDate);
    createTaskDTO.setTitle(inputTaskTitle.getText().toString());
    createTaskDTO.setDescription(inputTaskDescription.getText().toString());
    createTaskDTO.setSubtaskList(subtaskList);
    createTaskDTO.setReminderType(reminderType);

    if (isForUpdate) {

      updateTask();
    } else {

      createTask();
    }

  }

  private void updateTask() {
    taskApiService.updateTask(taskId, createTaskDTO).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {

          Toast.makeText(CreateTaskActivity.this, "Task info updated", Toast.LENGTH_LONG).show();
          finish();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Log.e(TAG, "Error with task creating", t);
      }
    });
  }

  private void createTask() {
    taskApiService.createTask(createTaskDTO).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {
          setResult(RESULT_OK, null);
          finish();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

        Log.e(TAG, "Error with task creating", t);
      }
    });
  }

  @OnClick(R.id.backToTasksIcon)
  public void backToTasksList() {
    finish();
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    if (isDeadlineSelecting) {

      selectedDeadlineDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
    } else {

      selectedReminderDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
    }

    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme, this, hour, minute, true);
    timePickerDialog.show();
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    if (isDeadlineSelecting) {

      selectedDeadlineDate = selectedDeadlineDate.withHour(hourOfDay).withMinute(minute);
      inputDeadlineDate.setText(selectedDeadlineDate.format(DATE_TIME_FORMATTER));
    } else {

      selectedReminderDate = selectedReminderDate.withHour(hourOfDay).withMinute(minute);
      reminderTime.setText(selectedReminderDate.format(DATE_TIME_FORMATTER));
    }
  }

  @Override
  public void applyValues(String subtaskName, SubtaskPriority subtaskPriority) {

    Subtask newSubtask = new Subtask();
    newSubtask.setName(subtaskName);
    newSubtask.setPriority(subtaskPriority);

    subtaskList.add(newSubtask);
    subtaskAdapter.notifyDataSetChanged();
  }
}
