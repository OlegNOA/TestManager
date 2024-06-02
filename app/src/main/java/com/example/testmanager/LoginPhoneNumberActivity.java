package com.example.testmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtpBtn.setOnClickListener(v -> {
            if (!countryCodePicker.isValidFullNumber()) {
                phoneInput.setError("Phone number not valid");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            sendOtpBtn.setVisibility(View.GONE);

            String phoneNumber = countryCodePicker.getFullNumberWithPlus();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("phone", phoneNumber)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        sendOtpBtn.setVisibility(View.VISIBLE);

                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOtpActivity.class);
                            intent.putExtra("phone", phoneNumber);
                            saveUserDataToSharedPreferences(phoneNumber);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginPhoneNumberActivity.this, "Phone number not found in database", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
    private void saveUserDataToSharedPreferences(String phoneNumber) {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();
    }
}

