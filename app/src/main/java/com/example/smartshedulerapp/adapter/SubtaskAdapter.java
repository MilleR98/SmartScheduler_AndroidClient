package com.example.smartshedulerapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.dialog.InviteMemberDialog.InviteMemberDialogListener;
import com.example.smartshedulerapp.model.Subtask;
import com.example.smartshedulerapp.model.type.SubtaskPriority;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubtaskAdapter extends BaseAdapter {

  private final Context context;
  private List<Subtask> itemModelList;

  @Override
  public int getCount() {

    return itemModelList.size();
  }

  @Override
  public Object getItem(int position) {

    return itemModelList.get(position);
  }

  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    convertView = mInflater.inflate(R.layout.subtask_item, null);
    TextView subtaskName = convertView.findViewById(R.id.subtaskName);
    ImageView imgRemove = convertView.findViewById(R.id.imgRemove);
    ImageView imgPriority = convertView.findViewById(R.id.priorityIcon);

    Subtask subtask = itemModelList.get(position);
    subtaskName.setText(subtask.getName());

    int priorityIcon = getPriorityIcon(subtask.getPriority());
    imgPriority.setBackground(ContextCompat.getDrawable(context, priorityIcon));

    imgRemove.setOnClickListener(v -> {
      itemModelList.remove(position);
      notifyDataSetChanged();
    });

    return convertView;
  }

  private int getPriorityIcon(SubtaskPriority priority) {
    if (priority.equals(SubtaskPriority.LOW)) {

      return R.drawable.ic_arrow_upward_green_24dp;
    } else if (priority.equals(SubtaskPriority.MEDIUM)) {

      return R.drawable.ic_arrow_upward_yelow_24dp;
    } else {

      return R.drawable.ic_arrow_upward_red_24dp;
    }
  }

  public void setItemModelList(List<Subtask> itemModelList) {
    this.itemModelList = itemModelList;
  }
}
