package com.example.smartshedulerapp.adapter;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;
import static java.lang.String.format;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.ViewEventActivity;
import com.example.smartshedulerapp.api.EventApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.EventPreviewDTO;
import java.util.List;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventPreviewAdapter extends RecyclerView.Adapter<EventPreviewAdapter.ViewHolder> {

  private final Context context;
  private List<EventPreviewDTO> itemModelList;

  @Inject
  EventApiService taskApiService;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_preview_item, parent, false);

    TaskEventComponent taskEventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(parent.getContext())).build();
    taskEventComponent.inject(this);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    EventPreviewDTO eventPreviewDTO = itemModelList.get(position);

    ((TextView) holder.itemView.findViewById(R.id.eventDate)).setText(format("%s - %s",
        eventPreviewDTO.getStartTime().format(DATE_TIME_FORMATTER), eventPreviewDTO.getEndTime().format(DATE_TIME_FORMATTER)));
    ((TextView) holder.itemView.findViewById(R.id.eventMembersCount)).setText(format("%d members invited", eventPreviewDTO.getMembersCount()));
    ((TextView) holder.itemView.findViewById(R.id.eventTitle)).setText(eventPreviewDTO.getName());
    ((TextView) holder.itemView.findViewById(R.id.owner)).setText(eventPreviewDTO.getCurrentUserEventPermission().toString());

    holder.itemView.findViewById(R.id.owner).setOnClickListener(v -> {

      Intent intent = new Intent(context, ViewEventActivity.class);
      intent.putExtra("eventId", eventPreviewDTO.getEventId());
      context.startActivity(intent);
    });
  }

  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public int getItemCount() {

    return itemModelList.size();
  }

  public void setItemModelList(List<EventPreviewDTO> itemModelList) {
    this.itemModelList = itemModelList;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout linearLayout;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

    }
  }
}
