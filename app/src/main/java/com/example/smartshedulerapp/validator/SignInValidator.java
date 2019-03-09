package com.example.smartshedulerapp.validator;

import android.util.Patterns;
import android.widget.EditText;

public class SignInValidator {

  public static boolean validate(EditText emailText, EditText passwordText) {
    boolean valid = true;

    String email = emailText.getText().toString();
    String password = passwordText.getText().toString();

    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      emailText.setError("Enter a valid email address");
      valid = false;

    } else {
      emailText.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      passwordText.setError("between 4 and 10 alphanumeric characters");
      valid = false;

    } else {
      passwordText.setError(null);
    }

    return valid;
  }
}
