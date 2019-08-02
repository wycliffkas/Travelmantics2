package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> deals;
    
    private FirebaseUtil(){};
    
    public static void openReference(String ref){
        if(firebaseUtil == null){
           firebaseUtil = new FirebaseUtil();
           firebaseDatabase = FirebaseDatabase.getInstance();
        }
    
        deals = new ArrayList<TravelDeal>();
        databaseReference = firebaseDatabase.getReference().child(ref);
    }
    
}
