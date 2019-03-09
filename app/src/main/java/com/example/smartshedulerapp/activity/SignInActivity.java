package com.example.smartshedulerapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.smartshedulerapp.R;
import com.example.smartshedulerapp.api.AuthApiService;
import com.example.smartshedulerapp.config.AuthComponent;
import com.example.smartshedulerapp.config.DaggerAuthComponent;
import com.example.smartshedulerapp.model.SignInDTO;
import com.example.smartshedulerapp.model.UserDetails;
import com.example.smartshedulerapp.validator.SignInValidator;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = "LoginActivity";
  private static final int REQUEST_SIGNUP = 0;

  @Inject
  AuthApiService authApiService;

  @BindView(R.id.emailInput)
  EditText emailText;
  @BindView(R.id.passwordInput)
  EditText passwordText;
  @BindView(R.id.signInButton)
  Button loginButton;
  @BindView(R.id.linkSignUp)
  TextView signupLink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);
    ButterKnife.bind(this);

    AuthComponent authComponent = DaggerAuthComponent.create();
    authComponent.inject(this);
  }

  @OnClick(R.id.linkSignUp)
  public void openSignUpPage() {
    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
    startActivityForResult(intent, REQUEST_SIGNUP);
  }

  @OnClick(R.id.signInButton)
  public void login() {
    Log.d(TAG, "Login");

    if (!SignInValidator.validate(emailText, passwordText)) {
      onLoginFailed();

      return;
    }

    loginButton.setEnabled(false);

    final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,
        R.style.Theme_MaterialComponents_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Authenticating...");
    progressDialog.show();

    SignInDTO signInDTO = new SignInDTO();
    signInDTO.setEmail(emailText.getText().toString());
    signInDTO.setPassword(passwordText.getText().toString());

    authApiService.signIn(signInDTO).enqueue(new Callback<UserDetails>() {

      @Override
      public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {

        if (response.isSuccessful()) {

          onLoginSuccess();
        } else {

          onLoginFailed();
        }
        progressDialog.dismiss();

      }

      @Override
      public void onFailure(Call<UserDetails> call, Throwable t) {
        progressDialog.dismiss();
        onLoginFailed();
      }
    });
  }


  @Override
  public void onBackPressed() {

    moveTaskToBack(true);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_SIGNUP) {
      if (resultCode == RESULT_OK) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Successfully sign up")
            .setMessage("Please login to your new account")
            .setIcon(R.drawable.ic_account_circle_black_36dp)
            .setCancelable(false)
            .setNegativeButton("OK", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();

        this.finish();
      }
    }
  }

  public void onLoginSuccess() {
    loginButton.setEnabled(true);
    finish();
  }

  public void onLoginFailed() {
    Toast.makeText(getBaseContext(), "Invalid email or password", Toast.LENGTH_LONG).show();

    loginButton.setEnabled(true);
  }
}
