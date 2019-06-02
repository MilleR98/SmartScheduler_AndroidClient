package com.example.smartshedulerapp.adapter;

import static java.lang.String.format;

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
import com.example.smartshedulerapp.model.EventMemberDTO;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberAdapter extends BaseAdapter {

  private final Context context;
  private List<EventMemberDTO> membersList;

  public void setMembersList(List<EventMemberDTO> membersList) {
    this.membersList = membersList;
  }

  @Override
  public int getCount() {

    return membersList.size();
  }

  @Override
  public Object getItem(int position) {

    return membersList.get(position);
  }

  @Override
  public long getItemId(int position) {

    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    convertView = mInflater.inflate(R.layout.invited_member_item, null);
    TextView fullName = convertView.findViewById(R.id.fullNameLabel);
    TextView email = convertView.findViewById(R.id.emailLabel);
    ImageView imgRemove = convertView.findViewById(R.id.imgRemove);
    ImageView permissionIcon = convertView.findViewById(R.id.permissionIcon);

    EventMemberDTO eventMember = membersList.get(position);

    fullName.setText(format("%s %s", eventMember.getFirstName(), eventMember.getLastName()));
    email.setText(eventMember.getMemberEmail());

    int permissionIconId = getPermissionIcon(eventMember.getMemberPermission());
    permissionIcon.setBackground(ContextCompat.getDrawable(context, permissionIconId));

    imgRemove.setOnClickListener(v -> {
      membersList.remove(position);
      notifyDataSetChanged();
    });

    return convertView;
  }

  private int getPermissionIcon(EventMemberPermission eventMemberPermission) {
    if (eventMemberPermission.equals(EventMemberPermission.EDITOR)) {

      return R.drawable.ic_edit_black_24dp;
    } else {

      return R.drawable.ic_visibility_black_24dp;
    }
  }
}