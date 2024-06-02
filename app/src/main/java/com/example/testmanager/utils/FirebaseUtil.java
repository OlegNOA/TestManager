package com.example.testmanager.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference getUserDetails(String userId) {
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }
    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        String userId = currentUserId();
        if (userId != null) {
            return FirebaseFirestore.getInstance().collection("users").document(userId);
        } else {
            return null; // or throw an exception, or handle this case according to your app logic
        }
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
}
