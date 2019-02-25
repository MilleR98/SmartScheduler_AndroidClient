package com.example.smartshedulerapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshedulerapp.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.firstNameInput)
    EditText firstNameInput;
    @BindView(R.id.lastNameInput)
    EditText lastNameInput;
    @BindView(R.id.emailInput)
    EditText emailText;
    @BindView(R.id.passwordInput)
    EditText passwordText;
    @BindView(R.id.confirmPasswordInput)
    EditText confirmPassword;
    @BindView(R.id.signUnButton)
    Button signUpBtn;
    @BindView(R.id.linkSignIn)
    TextView signinpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signUnButton)
    public void signUp() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signUpBtn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.Theme_MaterialComponents_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String cofirmPsw = confirmPassword.getText().toString();

        // TODO: signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupSuccess() {
        signUpBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signUpBtn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String cofirmPsw = confirmPassword.getText().toString();

        // TODO sign up validation

        return valid;
    }

    @OnClick(R.id.linkSignIn)
    public void openLoginScreen() {
        finish();
    }
}
