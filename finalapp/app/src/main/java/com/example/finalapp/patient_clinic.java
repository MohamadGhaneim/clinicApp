package com.example.finalapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class patient_clinic extends AppCompatActivity {
    ImageButton back;
    Button search;
    EditText  patient_fnumber;
    Spinner clinic_name;
    private DatabaseReference database;
    public patient_clinic(){
        database = FirebaseDatabase.getInstance().getReference();
        select_apointment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_clinic);

        back= findViewById(R.id.ib_back_patient);
        search = findViewById(R.id.btn_search_app);
        patient_fnumber = findViewById(R.id.et_patient_fnumber);
        clinic_name = findViewById(R.id.sp_clinic_forpatient);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedClinic = clinic_name.getSelectedItem().toString().trim();
                if(patient_fnumber.getText().toString().trim().isEmpty() || selectedClinic.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(patient_clinic.this);
                    builder.setTitle("warnnig").setMessage("all fields are required").setPositiveButton("OK", null).show();
                }
                else{
                    StringBuffer sb= new StringBuffer();
                    database = FirebaseDatabase.getInstance().getReference();
                    database.child(clinic_name.getSelectedItem().toString()).child("patient").
                            child(patient_fnumber.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ds) {
                                    if (ds.exists()){

                                        String patientName = ds.child("name").getValue(String.class);
                                        String patientNum = ds.child("number").getValue(String.class);
                                        String sessionType = ds.child("session_type").getValue(String.class);
                                        String date = ds.child("date").getValue(String.class);

                                        sb.append("Hi ").append(patientName).append(" '").append(patientNum).append("'\n");
                                        sb.append("you have a session of :").append(sessionType).append("\n");
                                        sb.append("date :").append(date).append("\n");


                                        AlertDialog.Builder b = new AlertDialog.Builder(patient_clinic.this);
                                        b.setTitle("your appointment").setMessage(sb.toString()).
                                                setIcon(R.drawable.health).setPositiveButton("ok",null).show();
                                    }
                                    else{
                                        Toast.makeText(patient_clinic.this,"file not found" ,Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(patient_clinic.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void select_apointment() {
        List clinic_list = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()) {
                    String clinicName = s.getKey();
                    clinic_list.add(clinicName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(patient_clinic.this ,android.R.layout.simple_spinner_dropdown_item , clinic_list );
                clinic_name.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}