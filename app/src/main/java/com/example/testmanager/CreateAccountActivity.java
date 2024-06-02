package com.example.testmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmanager.model.UserModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText, phoneInput, usernameInput;
    private Button createAccountBtn;
    private ProgressBar progressBar;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initUI();

        createAccountBtn.setOnClickListener(v -> createAccount());

        TextView loginTextView = findViewById(R.id.login_text_view_btn);

        loginTextView.setOnClickListener(v -> navigateToLoginActivity());



    }


    private void navigateToLoginActivity() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initUI() {
        usernameInput = findViewById(R.id.create_username);
        countryCodePicker = findViewById(R.id.create_countrycode);
        phoneInput = findViewById(R.id.create_mobile_number);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String username = usernameInput.getText().toString();
        String phoneNumber = phoneInput.getText().toString();
        String countryCode = countryCodePicker.getSelectedCountryCode();
        String mobile = "+" + countryCode + phoneNumber;

        if (!validateData(email, password, confirmPassword)) {
            return;
        }

        createAccountInFirebase(email, password, username, mobile);
    }

    private void createAccountInFirebase(String email, String password, String username, String mobile) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            changeInProgress(false);
            if (task.isSuccessful()) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();

                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(profileUpdateTask -> {
                            if (profileUpdateTask.isSuccessful()) {
                                UserModel userModel = new UserModel(mobile, username, email, password, userId, Timestamp.now());
                                userModel.setUsername(username);
                                userModel.setPhone(mobile);
                                saveUserToFirestore(userModel);

                            } else {
                                Toast.makeText(CreateAccountActivity.this, "Failed to update profile: " + profileUpdateTask.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToFirestore(UserModel userModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).set(userModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(CreateAccountActivity.this, "Successfully created account, Check email to verify", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                FirebaseAuth.getInstance().signOut();
                finish();
            } else {
                Toast.makeText(CreateAccountActivity.this, "Failed to store user data: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        createAccountBtn.setVisibility(inProgress ? View.GONE : View.VISIBLE);
    }

    private boolean validateData(String email, String password, String confirmPassword) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password length is invalid");
            return false;
        }
        if (password.length() < 8 || !Pattern.compile("[a-zA-Z]").matcher(password).find() ||
                !Pattern.compile("[0-9]").matcher(password).find() ||
                !Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            passwordEditText.setError("Password must be at least 8 characters long, contain letters, numbers, and special characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password not matched");
            return false;
        }
        return true;
    }
}