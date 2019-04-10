package com.example.smartshedulerapp.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.model.type.EventMemberPermission;
import com.example.smartshedulerapp.model.type.SubtaskPriority;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InviteMemberDialog extends AppCompatDialogFragment {

  @BindView(R.id.firstNameInput)
  EditText firstNameInput;
  @BindView(R.id.lastNameInput)
  EditText lastNameInput;
  @BindView(R.id.emailInput)
  EditText emailInput;
  @BindView(R.id.memberPermissionGroup)
  RadioGroup memberPermissionGroup;
  @BindView(R.id.canInviteCheckbox)
  CheckBox canInviteCheckbox;

  private InviteMemberDialogListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = new Builder(getActivity());

    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.invite_member_dialog, null);

    ButterKnife.bind(this, view);

    builder.setTitle("Invite new member")
        .setView(view)
        .setNegativeButton("Cancel", (dialogInterface, i) -> {

        })
        .setPositiveButton("Submit", (dialogInterface, i) -> {
          String firstName = firstNameInput.getText().toString();
          String lastName = lastNameInput.getText().toString();
          String email = emailInput.getText().toString();
          boolean canInvite = canInviteCheckbox.isChecked();

          int checkedRadioButtonId = memberPermissionGroup.getCheckedRadioButtonId();

          EventMemberPermission eventMemberPermission = EventMemberPermission.VIEWER;

          if (checkedRadioButtonId == R.id.editorPermission) {

            eventMemberPermission = EventMemberPermission.EDITOR;
          }

          listener.applyValues(firstName, lastName, email, eventMemberPermission, canInvite);
        });

    return builder.create();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    try {
      listener = (InviteMemberDialogListener) context;
    } catch (ClassCastException e) {

      throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
    }
  }

  public interface InviteMemberDialogListener {

    void applyValues(String firstName, String lastName, String email, EventMemberPermission eventMemberPermission, boolean canInvite);
  }
}
