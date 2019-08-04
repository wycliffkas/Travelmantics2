package com.example.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static FirebaseStorage storage;
    public static StorageReference storageRef;
    public static ArrayList<TravelDeal> deals;
    private static ListDealActivity caller;
    public static boolean isAdmin;
    
    private FirebaseUtil(){};
    
    public static void openReference(String ref, final ListDealActivity callerActivity){
        if(firebaseUtil == null){
           firebaseUtil = new FirebaseUtil();
           firebaseDatabase = FirebaseDatabase.getInstance();
           firebaseAuth = FirebaseAuth.getInstance();
           caller = callerActivity;
           authStateListener = new FirebaseAuth.AuthStateListener() {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   // Choose authentication providers
                   
                   if(firebaseAuth.getCurrentUser() == null){
                       FirebaseUtil.signIn();
                   } else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                   }

                   Toast.makeText(callerActivity.getBaseContext(), "Welcome Back",
                           Toast.LENGTH_LONG).show();
               }
           };
        }
    
        deals = new ArrayList<TravelDeal>();
        databaseReference = firebaseDatabase.getReference().child(ref);
        connectStorage();
    }
    
    private static void checkAdmin(String userId) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = firebaseDatabase.getReference().child("admins").child(userId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
            }
    
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        
            }
    
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        
            }
    
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
        
            }
        };
        ref.addChildEventListener(listener);
        
    }
    
    private static void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
    
    public static void attachListener(){
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    
    public static void dettachListener(){
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
    
    public static void connectStorage() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("deals_pictures");
    }
    
}
