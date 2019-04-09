package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.activity.CreateEventActivity;
import com.example.smartshedulerapp.activity.CreateTaskActivity;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.module.UserEventApiModule;
import com.example.smartshedulerapp.di_config.module.UserTaskApiModule;
import com.example.smartshedulerapp.fragment.DailyEventsFragment;
import com.example.smartshedulerapp.fragment.TasksListFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {UserEventApiModule.class, UserTaskApiModule.class})
public interface TaskEventComponent {

  EventApiService getAuthApiService();

  TaskApiService getTaskApiService();

  void inject(CreateTaskActivity createTaskActivity);

  void inject(CreateEventActivity createEventActivity);

  void inject(TasksListFragment tasksListFragment);

  void inject(DailyEventsFragment dailyEventsFragment);
}
