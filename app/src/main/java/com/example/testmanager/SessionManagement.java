package com.example.testmanager;

import android.content.Context;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class SessionManagement {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Context mContext;

    public class User {
        public String id;
        public String email;
        public String username;
        public String password;

        public User() {
            // Пустой конструктор необходим для вызова DataSnapshot.getValue(User.class)
        }

        public User(String id, String email, String username, String password) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }
    public SessionManagement(Context context) {
        this.mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    public void createLoginSession(String id, String email, String username, String password) {
        // Сохранение данных пользователя в Firebase Realtime Database
        String userId = mAuth.getCurrentUser().getUid();
        User user = new User(id, email, username, password);
        mDatabase.child(userId).setValue(user);
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void checkIsLogin(Context mContext) {
        if (!isLoggedIn()) {
            Intent i = new Intent(mContext, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    public void logoutUser() {
        mAuth.signOut();
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}

