package com.example.smartshedulerapp.activity;

import android.app.ProgressDialog;
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
import com.example.smartshedulerapp.di_config.module.AppModule;
import com.example.smartshedulerapp.di_config.component.AuthComponent;
import com.example.smartshedulerapp.di_config.DaggerAuthComponent;
import com.example.smartshedulerapp.model.SignUpDTO;
import com.example.smartshedulerapp.validator.SignUpValidator;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

  private static final String TAG = "SignupActivity";

  @Inject
  AuthApiService authApiService;

  @BindView(R.id.firstNameInput)
  EditText firstNameInput;
  @BindView(R.id.lastNameInput)
  EditText lastNameInput;
  @BindView(R.id.emailInput)
  EditText emailInput;
  @BindView(R.id.phoneNumberInput)
  EditText phoneNumberInput;
  @BindView(R.id.passwordInput)
  EditText passwordInput;
  @BindView(R.id.confirmPasswordInput)
  EditText confirmPasswordInput;
  @BindView(R.id.signUnButton)
  Button signUpBtn;
  @BindView(R.id.linkSignIn)
  TextView signinpLink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    ButterKnife.bind(this);

    AuthComponent authComponent = DaggerAuthComponent.builder().appModule(new AppModule(this)).build();
    authComponent.inject(this);
  }

  @OnClick(R.id.signUnButton)
  public void signUp() {
    Log.d(TAG, "Signup");

    if (!SignUpValidator.validate(firstNameInput, lastNameInput, emailInput, phoneNumberInput, passwordInput, confirmPasswordInput)) {
      onSignupFailed();

      return;
    }

    signUpBtn.setEnabled(false);

    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
        R.style.Theme_MaterialComponents_Dialog);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Creating Account...");
    progressDialog.show();

    SignUpDTO signUpDTO = new SignUpDTO();
    signUpDTO.setFirstName(firstNameInput.getText().toString());
    signUpDTO.setLastName(lastNameInput.getText().toString());
    signUpDTO.setEmail(emailInput.getText().toString());
    signUpDTO.setPhoneNumber(phoneNumberInput.getText().toString());
    signUpDTO.setPassword(passwordInput.getText().toString());
    signUpDTO.setRepeatPassword(confirmPasswordInput.getText().toString());

    authApiService.signUp(signUpDTO).enqueue(new Callback<ResponseBody>() {

      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {

          onSignupSuccess();
        } else {

          onSignupFailed();
        }

        progressDialog.dismiss();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        onSignupFailed();
        progressDialog.dismiss();
      }
    });
  }

  public void onSignupSuccess() {
    signUpBtn.setEnabled(true);
    setResult(RESULT_OK, null);
    finish();
  }

  public void onSignupFailed() {
    Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

    signUpBtn.setEnabled(true);
  }

  @OnClick(R.id.linkSignIn)
  public void openLoginScreen() {
    finish();
  }
}
