package com.example.finalapp;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class patient_db {
    private DatabaseReference database;
    public  patient_db(){
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void add_patient(String clinic ,String number_file ,String name , String phone ,String type , String date){
        Map<String, String> patientData = new HashMap<>();
        patientData.put("name", name);
        patientData.put("number", phone);
        patientData.put("session_type", type);
        patientData.put("date", date);
        database.child(clinic).child("patient").child(number_file).setValue(patientData);
    }
    public void update_patient(String clinic ,String number_file ,String name , String phone ,String type , String date){
        Map<String, Object> patientData = new HashMap<>();
        patientData.put("name", name);
        patientData.put("number", phone);
        patientData.put("session_type", type);
        patientData.put("date", date);
        database.child(clinic).child("patient").child(number_file).updateChildren(patientData);
    }

    public void delete_patient(String clinic ,String number_file ){
        database.child(clinic).child("patient").child(number_file).removeValue();
    }



}
