package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.api.NotificationApiService;
import com.example.smartshedulerapp.di_config.module.NotificationApiModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = NotificationApiModule.class)
public interface NotificationComponent {

  NotificationApiService getNotificationApiService();
}
