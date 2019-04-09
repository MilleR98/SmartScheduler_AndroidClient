package com.example.smartshedulerapp.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.model.type.SubtaskPriority;

public class CreateSubtaskDialog extends AppCompatDialogFragment {

  @BindView(R.id.subtaskNameInput)
  EditText subtaskNameInput;
  @BindView(R.id.subtaskPriorityGroup)
  RadioGroup subtaskPriorityGroup;

  private ExampleDialogListener listener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new Builder(getActivity());

    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.subtask_create_dialog, null);

    ButterKnife.bind(this, view);

    builder.setTitle("Create subtask")
        .setView(view)
        .setNegativeButton("Cancel", (dialogInterface, i) -> {

        })
        .setPositiveButton("Submit", (dialogInterface, i) -> {
          String subtaskName = subtaskNameInput.getText().toString();
          int checkedRadioButtonId = subtaskPriorityGroup.getCheckedRadioButtonId();

          SubtaskPriority subtaskPriority = SubtaskPriority.MEDIUM;

          if (checkedRadioButtonId == R.id.lowPriority) {

            subtaskPriority = SubtaskPriority.LOW;
          } else if (checkedRadioButtonId == R.id.hightPriority) {

            subtaskPriority = SubtaskPriority.HIGH;
          }

          listener.applyValues(subtaskName, subtaskPriority);
        });

    return builder.create();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    try {
      listener = (ExampleDialogListener) context;
    } catch (ClassCastException e) {

      throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
    }
  }

  public interface ExampleDialogListener {

    void applyValues(String subtaskName, SubtaskPriority subtaskPriority);
  }
}
