package com.example.smartshedulerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.ChallengeApiService;
import com.example.smartshedulerapp.api.NotificationApiService;
import com.example.smartshedulerapp.di_config.component.ChallengeComponent;
import com.example.smartshedulerapp.di_config.component.DaggerNotificationComponent;
import com.example.smartshedulerapp.di_config.component.NotificationComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import javax.inject.Inject;

public class NotificationsFragment extends Fragment {

  @Inject
  NotificationApiService notificationApiService;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.notifications_fragment, container, false);
    ButterKnife.bind(this, view);

    NotificationComponent notificationComponent = DaggerNotificationComponent.builder().appModule(new AppModule(getContext())).build();
    notificationComponent.inject(this);

    return view;
  }
}
