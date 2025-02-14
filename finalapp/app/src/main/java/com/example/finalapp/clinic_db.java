package com.example.finalapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class clinic_db {
    private DatabaseReference database;

    // Constructor to initialize Firebase reference
    public clinic_db(){
        // Initialize the database reference
        database = FirebaseDatabase.getInstance().getReference();
    }
    public void add_clinic_info(String dr_name ,String dr_number,String drpass ,String clinic_name){

        Map<String, String> doctor_info = new HashMap<>();
        doctor_info.put("name", dr_name);
        doctor_info.put("number", dr_number);
        doctor_info.put("pass", drpass);
        database.child(clinic_name).child("doctor").setValue(doctor_info);

    }


}