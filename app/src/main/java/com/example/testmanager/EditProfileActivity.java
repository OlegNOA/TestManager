package com.example.testmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testmanager.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EditProfileActivity extends AppCompatActivity {

    private EditText phoneEditText, emailEditText;
    private FirebaseAuth mAuth;
    private ImageButton backButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initUI();

        phoneEditText.setFocusable(false);
        phoneEditText.setClickable(false);

        emailEditText.setFocusable(false);
        emailEditText.setClickable(false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            loadUserDataFromSharedPreferences();
            loadUserData(currentUser.getUid());
        } else {
            Toast.makeText(EditProfileActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
        }

        backButton = findViewById(R.id.back_main);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut(); // Вызываем метод выхода из аккаунта
            }
        });
    }
    private void signOut() {
        mAuth.signOut(); // Метод Firebase для выхода из аккаунта

        // После выхода из аккаунта, переходите на LoginActivity
        Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Очищаем стек активностей
        startActivity(intent);
        finish(); // Закрываем текущую активность, чтобы пользователь не мог вернуться назад
    }

    private void initUI() {
        phoneEditText = findViewById(R.id.edit_mobile_number);
        emailEditText = findViewById(R.id.edit_email);

    }

    private void loadUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (true) {
                            DocumentSnapshot document = task.getResult();
                            if (true) {


                                CollectionReference usersRef = db.collection("users");

                                usersRef.get().addOnCompleteListener(querySnapshot -> {
                                    if (querySnapshot.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : querySnapshot.getResult()) {
                                            System.out.println(document.getId() + " => " + document1.getData());
                                        }
                                    } else {
                                        System.out.println("Ошибка при получении документов: " + querySnapshot.getException());
                                    }
                                });


                                UserModel userModel = document.toObject(UserModel.class);
                                if (userModel != null) {
                                    String phone = userModel.getPhone();
                                    String email = userModel.getEmail();
                                    phoneEditText.setText(phone);
                                    emailEditText.setText(email);
                                }
                            } else {
                                Toast.makeText(EditProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to load user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadUserDataFromSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String phoneNumber = preferences.getString("phoneNumber", "");

        phoneEditText.setText(phoneNumber);
        emailEditText.setText(email);

    }
}
