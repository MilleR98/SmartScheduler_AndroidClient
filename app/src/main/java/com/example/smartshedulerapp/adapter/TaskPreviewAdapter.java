package com.example.smartshedulerapp.adapter;

import static com.example.smartshedulerapp.util.Constants.DATE_TIME_FORMATTER;
import static java.lang.String.format;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.activity.ViewEventActivity;
import com.example.smartshedulerapp.activity.ViewTaskActivity;
import com.example.smartshedulerapp.api.TaskApiService;
import com.example.smartshedulerapp.di_config.component.DaggerTaskEventComponent;
import com.example.smartshedulerapp.di_config.component.TaskEventComponent;
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.model.TaskPreviewDTO;
import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskPreviewAdapter extends RecyclerView.Adapter<ViewHolder> {

  @Inject
  TaskApiService taskApiService;

  private final Context context;
  private List<TaskPreviewDTO> itemModelList;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_preview_item, parent, false);

    TaskEventComponent taskEventComponent = DaggerTaskEventComponent.builder().appModule(new AppModule(parent.getContext())).build();
    taskEventComponent.inject(this);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    TaskPreviewDTO taskPreview = itemModelList.get(position);

    String createdAt = taskPreview.getCreatedAt().format(DATE_TIME_FORMATTER);
    String deadlineDate = taskPreview.getDeadlineDate().format(DATE_TIME_FORMATTER);

    int subtasksCount = taskPreview.getSubtaskStatuses().size();

    long completerTasks = taskPreview.getSubtaskStatuses()
        .stream()
        .filter(subtaskStatus -> subtaskStatus.equals(SubtaskStatus.COMPLETED))
        .count();

    ((TextView) holder.itemView.findViewById(R.id.taskPreviewTitle)).setText(taskPreview.getTitle());
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewProgress)).setText(format("Progress: %s of %s", completerTasks ,subtasksCount));
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewCreatedAt)).setText(format("Created at: %s", createdAt));
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewDeadline)).setText(format("Deadline: %s", deadlineDate));

    holder.itemView.findViewById(R.id.itemPreviewLayout).setOnClickListener(v -> {

      Intent intent = new Intent(context, ViewTaskActivity.class);
      intent.putExtra("taskId", taskPreview.getId());
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

  public void setItemModelList(List<TaskPreviewDTO> itemModelList) {
    this.itemModelList = itemModelList;
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
}
