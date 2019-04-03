package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.module.UserEventApiModule;
import com.example.smartshedulerapp.di_config.module.UserTaskApiModule;
import com.example.smartshedulerapp.fragment.HomeFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {UserEventApiModule.class, UserTaskApiModule.class})
public interface TaskEventComponent {

  EventApiService getAuthApiService();

  TaskApiService getTaskApiService();

  void inject(HomeFragment homeFragment);
}
