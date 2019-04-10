package com.example.smartshedulerapp.activity;

import static android.content.ContentValues.TAG;
import static com.example.smartshedulerapp.model.type.ReminderType.DAILY;
import static com.example.smartshedulerapp.model.type.ReminderType.ONE_TIME;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
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
import com.example.smartshedulerapp.model.type.ReminderType;
import com.example.smartshedulerapp.model.type.SubtaskPriority;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
    CreateSubtaskDialog.ExampleDialogListener {

  @Inject
  TaskApiService taskApiService;

  private Date selectedDeadlineDate;
  private String selectedDeadlineDateString;
  private Date selectedReminderDate;
  private String selectedReminderDateString;
  boolean isDealineSelecting;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_task);

    ButterKnife.bind(this);

    TaskEventComponent authComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(this)).build();
    authComponent.inject(this);

    subtaskAdapter = new SubtaskAdapter(getApplicationContext(), subtaskList);
    subtaskListView.setAdapter(subtaskAdapter);
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
    isDealineSelecting = true;

    openDatePickerDialog();
  }

  @OnClick(R.id.reminderTime)
  public void inputReminderTimeClick() {
    isDealineSelecting = false;

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

    ReminderType reminderType = checkedRadioButtonId == R.id.dailyNotification ? DAILY : ONE_TIME;

    CreateTaskDTO createTaskDTO = new CreateTaskDTO();
    createTaskDTO.setDeadlineDate(selectedDeadlineDateString);
    createTaskDTO.setReminderTime(selectedReminderDateString);
    createTaskDTO.setTitle(inputTaskTitle.getText().toString());
    createTaskDTO.setDescription(inputTaskDescription.getText().toString());
    createTaskDTO.setSubtaskList(subtaskList);
    createTaskDTO.setReminderType(reminderType);

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
    if (isDealineSelecting) {

      selectedDeadlineDate = new Date(year, month, dayOfMonth);
    } else {

      selectedReminderDate = new Date(year, month, dayOfMonth);
    }

    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme, this, hour, minute, true);
    timePickerDialog.show();
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    if (isDealineSelecting) {

      selectedDeadlineDate.setHours(hourOfDay);
      selectedDeadlineDate.setMinutes(minute);
    } else {

      selectedReminderDate.setHours(hourOfDay);
      selectedReminderDate.setMinutes(minute);
    }

    Calendar instance = Calendar.getInstance();

    if (isDealineSelecting) {

      instance.set(selectedDeadlineDate.getYear(), selectedDeadlineDate.getMonth(), selectedDeadlineDate.getDay(), hourOfDay, minute);
      inputDeadlineDate.setText(DateFormat.format("yyyy-MM-dd HH:mm", instance));
      selectedDeadlineDateString = DateFormat.format("yyyy-MM-ddTHH:mm", instance).toString();
    } else {

      instance.set(selectedReminderDate.getYear(), selectedReminderDate.getMonth(), selectedReminderDate.getDay(), hourOfDay, minute);
      reminderTime.setText(DateFormat.format("yyyy-MM-dd HH:mm", instance));
      selectedReminderDateString = DateFormat.format("yyyy-MM-ddTHH:mm", instance).toString();
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
