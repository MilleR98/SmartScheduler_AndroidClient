package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.activity.CreateTaskActivity;
import com.example.smartshedulerapp.activity.ViewEventActivity;
import com.example.smartshedulerapp.activity.ViewTaskActivity;
import com.example.smartshedulerapp.adapter.EventPreviewAdapter;
import com.example.smartshedulerapp.adapter.TaskPreviewAdapter;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.module.UserEventApiModule;
import com.example.smartshedulerapp.di_config.module.UserTaskApiModule;
import com.example.smartshedulerapp.fragment.CalendarFragment;
import com.example.smartshedulerapp.fragment.TasksListFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {UserEventApiModule.class, UserTaskApiModule.class})
public interface TaskEventComponent {

  EventApiService getAuthApiService();

  TaskApiService getTaskApiService();

  void inject(CreateTaskActivity createTaskActivity);

  void inject(ViewTaskActivity viewTaskActivity);

  void inject(ViewEventActivity createTaskActivity);

  void inject(TasksListFragment tasksListFragment);

  void inject(CalendarFragment calendarFragment);

  void inject(TaskPreviewAdapter taskPreviewAdapter);

  void inject(EventPreviewAdapter eventPreviewAdapter);
}
