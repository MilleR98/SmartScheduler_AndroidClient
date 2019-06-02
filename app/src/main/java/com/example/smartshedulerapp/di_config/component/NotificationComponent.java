package com.example.smartshedulerapp.di_config.component;

import com.example.smartshedulerapp.api.NotificationApiService;
import com.example.smartshedulerapp.di_config.module.NotificationApiModule;
import com.example.smartshedulerapp.fragment.ChallengesFragment;
import com.example.smartshedulerapp.fragment.NotificationsFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = NotificationApiModule.class)
public interface NotificationComponent {

  NotificationApiService getNotificationApiService();

  void inject(NotificationsFragment notificationsFragment);
}
