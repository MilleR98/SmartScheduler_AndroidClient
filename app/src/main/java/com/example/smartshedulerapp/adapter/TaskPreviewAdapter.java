package com.example.smartshedulerapp.adapter;

import static java.lang.String.format;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.model.TaskPreviewDTO;
import com.example.smartshedulerapp.model.type.SubtaskStatus;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskPreviewAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<TaskPreviewDTO> itemModelList;

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_preview_item, parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    TaskPreviewDTO taskPreview = itemModelList.get(position);

    Calendar instance = Calendar.getInstance();

    Date createdAt = taskPreview.getCreatedAt();
    Date deadlineDate = taskPreview.getDeadlineDate();
    instance.set(createdAt.getYear(), createdAt.getMonth(), createdAt.getDay(), createdAt.getHours(), createdAt.getMinutes());
    String createdAtString = DateFormat.format("yyyy-MM-dd HH:mm", instance).toString();
    instance.set(deadlineDate.getYear(), deadlineDate.getMonth(), deadlineDate.getDay(), deadlineDate.getHours(), deadlineDate.getMinutes());
    String deadlineDateString = DateFormat.format("yyyy-MM-dd HH:mm", instance).toString();

    int subtasksCount = taskPreview.getSubtaskStatuses().size();
    long completerTasks = taskPreview.getSubtaskStatuses()
        .stream()
        .filter(subtaskStatus -> subtaskStatus.equals(SubtaskStatus.COMPLETED))
        .count();

    ((TextView) holder.itemView.findViewById(R.id.taskPreviewTitle)).setText(taskPreview.getTitile());
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewProgress)).setText(format("Progress: %s of %s", completerTasks, subtasksCount));
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewCreatedAt)).setText(createdAtString);
    ((TextView) holder.itemView.findViewById(R.id.taskPreviewDeadline)).setText(deadlineDateString);
  }


  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public int getItemCount() {

    return itemModelList.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {
      super(itemView);


    }
  }
}
