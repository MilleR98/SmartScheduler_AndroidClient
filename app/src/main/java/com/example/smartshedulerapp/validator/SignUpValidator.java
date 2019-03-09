package com.example.smartshedulerapp.validator;

import android.widget.EditText;

public class SignUpValidator {

  public static boolean validate(EditText firstNameInput, EditText lastNameInput,
      EditText emailInput, EditText phoneNumberInput, EditText passwordInput, EditText confirmPasswordInput) {

    boolean valid = true;

    String firstName = firstNameInput.getText().toString();
    String lastName = lastNameInput.getText().toString();
    String email = emailInput.getText().toString();
    String phoneNumber = phoneNumberInput.getText().toString();
    String password = passwordInput.getText().toString();
    String confirmPsw = confirmPasswordInput.getText().toString();

    if (firstName.isEmpty()) {
      firstNameInput.setError("This field is required");
      valid = false;
    } else {

      firstNameInput.setError(null);
    }

    if (lastName.isEmpty()) {
      lastNameInput.setError("This field is required");
      valid = false;
    } else {

      lastNameInput.setError(null);
    }

    if (phoneNumber.isEmpty()) {
      phoneNumberInput.setError("This field is required");
      valid = false;
    } else {

      phoneNumberInput.setError(null);
    }

    if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      emailInput.setError("Enter a valid email address");
      valid = false;
    } else {
      emailInput.setError(null);
    }

    if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
      passwordInput.setError("Must be between 4 and 10 alphanumeric characters");
      valid = false;
    } else {

      if (!confirmPsw.equals(password)) {
        confirmPasswordInput.setError("Password does not match");
        valid = false;

      } else {

        confirmPasswordInput.setError(null);
      }

      passwordInput.setError(null);
    }

    return valid;
  }
}
