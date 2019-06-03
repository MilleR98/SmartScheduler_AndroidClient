package com.example.smartshedulerapp.adapter;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.NotificationApiService;
import com.example.smartshedulerapp.di_config.component.DaggerNotificationComponent;
import com.example.smartshedulerapp.di_config.component.NotificationComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.Notification;
import com.example.smartshedulerapp.model.type.NotificationType;
import java.util.List;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiredArgsConstructor
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

  private final Context context;
  @Inject
  NotificationApiService notificationApiService;

  private List<Notification> itemModelList;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

    NotificationComponent notificationComponent = DaggerNotificationComponent.builder().appModule(new AppModule(parent.getContext())).build();
    notificationComponent.inject(this);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    Notification notification = itemModelList.get(position);

    ((TextView) holder.itemView.findViewById(R.id.notificationTime)).setText(notification.getCreatedAt().format(DATE_TIME_FORMATTER));
    ((TextView) holder.itemView.findViewById(R.id.notificationContent)).setText(notification.getContent());

    if (!notification.getNotificationType().equals(NotificationType.INVITATION)) {
      holder.itemView.findViewById(R.id.acceptInvitation).setVisibility(View.GONE);
      holder.itemView.findViewById(R.id.declineInvitation).setVisibility(View.GONE);
    } else {

      holder.itemView.findViewById(R.id.acceptInvitation).setOnClickListener(v -> {

        notificationApiService.acceptInvitation(notification.getAdditionalParameters().get("eventId")).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
              Toast.makeText(context, "Event added to your calendar", Toast.LENGTH_LONG).show();
            }

          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {

          }
        });
      });

      holder.itemView.findViewById(R.id.declineInvitation).setOnClickListener(v -> {

        notificationApiService.declineInvitation(notification.getAdditionalParameters().get("eventId")).enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {

          }
        });
      });
    }
  }

  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public int getItemCount() {

    return itemModelList.size();
  }

  public void setItemModelList(List<Notification> itemModelList) {
    this.itemModelList = itemModelList;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout linearLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

    }
  }
}
