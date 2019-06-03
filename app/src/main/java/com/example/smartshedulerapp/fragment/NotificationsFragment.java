package com.example.smartshedulerapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.adapter.NotificationsAdapter;
import com.example.smartshedulerapp.api.NotificationApiService;
import com.example.smartshedulerapp.di_config.component.DaggerNotificationComponent;
import com.example.smartshedulerapp.di_config.component.NotificationComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.Notification;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

  @Inject
  NotificationApiService notificationApiService;

  @BindView(R.id.notificationsRecycleView)
  RecyclerView notificationsRecycleView;

  RecyclerView.Adapter adapter;
  List<Notification> notificationList = new ArrayList<>();

  public NotificationsFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.notifications_fragment, container, false);
    ButterKnife.bind(this, view);

    NotificationComponent notificationComponent = DaggerNotificationComponent.builder().appModule(new AppModule(getContext())).build();
    notificationComponent.inject(this);

    notificationsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    adapter = new NotificationsAdapter(getContext());
    notificationsRecycleView.setAdapter(adapter);
    ((NotificationsAdapter) adapter).setItemModelList(notificationList);

    ProgressDialog progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Loading data...");
    progressDialog.show();

    fetchNotifications(progressDialog);

    return view;
  }

  private void fetchNotifications(ProgressDialog progressDialog) {

    notificationApiService.getUserNotifications().enqueue(new Callback<List<Notification>>() {
      @Override
      public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {

        progressDialog.dismiss();
        if (response.isSuccessful()) {

          notificationList = response.body();

          ((NotificationsAdapter) adapter).setItemModelList(notificationList);
          adapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onFailure(Call<List<Notification>> call, Throwable t) {

        progressDialog.dismiss();
      }
    });
  }
}
