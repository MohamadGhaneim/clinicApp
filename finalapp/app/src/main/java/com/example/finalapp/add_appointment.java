package com.example.finalapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.internal.zag;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class add_appointment extends AppCompatActivity {
    ImageButton back;
    Button add_patient , update , delete , display_all , one_patient;
    EditText  file_number ,patient_name , patient_number , session_type;
    DatePicker patient_date;

    patient_db _query = new patient_db();
    private DatabaseReference database;

    public void add_appointment(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_appointment);

        back = findViewById(R.id.ib_back_to_clinic);
        update = findViewById(R.id.btn_update_patient);
        add_patient = findViewById(R.id.btn_regester_patient);
        delete = findViewById(R.id.btn_delete_patient);
        display_all = findViewById(R.id.btn_all_patient);
        one_patient = findViewById(R.id.btn_one_patient);

        file_number = findViewById(R.id.et_file_number);
        patient_name = findViewById(R.id.et_patient_name);
        patient_number= findViewById(R.id.et_addpatient_number);
        session_type = findViewById(R.id.et_session_type);
        patient_date = findViewById(R.id.dp_date_appointment);



        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String clinic_name = intent.getStringExtra("CLINIC_NAME");
                if (clinic_name == null) {
                    Toast.makeText(add_appointment.this, "Clinic name not found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fileNum = file_number.getText().toString().trim();
                String patientName = patient_name.getText().toString().trim();
                String patientNum = patient_number.getText().toString().trim();
                String sessionType = session_type.getText().toString().trim();
                int day = patient_date.getDayOfMonth();
                int month = patient_date.getMonth() + 1; //index from 0
                int year = patient_date.getYear();
                String selectedDate = day + "/" + month + "/" + year;

                if (fileNum.isEmpty() || patientName.isEmpty() || patientNum.isEmpty() || sessionType.isEmpty() ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_appointment.this);
                    builder.setTitle("warnnig")
                            .setMessage("all fields are required")
                            .setPositiveButton("OK", null).show();
                }
                else{

                    database = FirebaseDatabase.getInstance().getReference();
                    database.child(clinic_name).child("patient").child(fileNum).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(add_appointment.this, "number file all ready exsist", Toast.LENGTH_SHORT).show();
                            }
                            else{
                               _query.add_patient(clinic_name ,fileNum,patientName ,patientNum,sessionType,selectedDate);
                                Toast.makeText(add_appointment.this, "patient added", Toast.LENGTH_SHORT).show();
                                clean();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(add_appointment.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(add_appointment.this,clinic.class);
                startActivity(i);
                finish();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String clinic_name = intent.getStringExtra("CLINIC_NAME");
                if (clinic_name == null) {
                    Toast.makeText(add_appointment.this, "Clinic name not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fileNum = file_number.getText().toString().trim();
                String patientName = patient_name.getText().toString().trim();
                String patientNum = patient_number.getText().toString().trim();
                String sessionType = session_type.getText().toString().trim();
                int day = patient_date.getDayOfMonth();
                int month = patient_date.getMonth() + 1; //index from 0
                int year = patient_date.getYear();
                String selectedDate = day + "/" + month + "/" + year;

                if(fileNum.isEmpty() || patientName.isEmpty() || patientNum.isEmpty() || sessionType.isEmpty() ){
                    Toast.makeText(add_appointment.this , "all fields are required", Toast.LENGTH_LONG).show();
                }
                else{
                    database = FirebaseDatabase.getInstance().getReference();
                    database.child(clinic_name).child("patient").child(fileNum).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                _query.update_patient(clinic_name ,fileNum,patientName ,patientNum,sessionType,selectedDate);
                                Toast.makeText(add_appointment.this, "patient updated", Toast.LENGTH_SHORT).show();
                                clean();
                            }
                            else{
                                Toast.makeText(add_appointment.this, "file number not exsist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String clinic_name = intent.getStringExtra("CLINIC_NAME");

                if (clinic_name == null) {
                    Toast.makeText(add_appointment.this, "Clinic name not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fileNum = file_number.getText().toString();
                int day = patient_date.getDayOfMonth();
                int month = patient_date.getMonth() + 1; //index from 0
                int year = patient_date.getYear();
                String selectedDate = day + "/" + month + "/" + year;
                if(fileNum.isEmpty()){
                    Toast.makeText(add_appointment.this , "file is required", Toast.LENGTH_LONG).show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_appointment.this);
                    builder.setTitle("warnnig")
                            .setMessage("are you sure")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    database = FirebaseDatabase.getInstance().getReference();
                                    database.child(clinic_name).child("patient").child(fileNum).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                _query.delete_patient(clinic_name ,fileNum);
                                                Toast.makeText(add_appointment.this, "patient deleted", Toast.LENGTH_SHORT).show();
                                                clean();
                                            }
                                            else{
                                                Toast.makeText(add_appointment.this, "file number not exsist", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(add_appointment.this, "Deletion cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }).show();

                }
            }
        });

        display_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String clinic_name = intent.getStringExtra("CLINIC_NAME");

                if (clinic_name == null) {
                    Toast.makeText(add_appointment.this, "Clinic name not found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                    StringBuffer sb= new StringBuffer();
                    database = FirebaseDatabase.getInstance().getReference();
                    database.child(clinic_name).child("patient").addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot s : snapshot.getChildren()) {
                                sb.append("file number :"+s.getKey()+"\n");

                                String patientName = s.child("name").getValue(String.class);
                                String patientNum = s.child("number").getValue(String.class);
                                String sessionType = s.child("session_type").getValue(String.class);
                                String date = s.child("date").getValue(String.class);

                                sb.append("Name :"+patientName+"\n");
                                sb.append("Number :"+patientNum+"\n");
                                sb.append("session type :"+sessionType+"\n");
                                sb.append("date :"+date+"\n");
                                sb.append("-------------------\n");
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(add_appointment.this);
                            builder.setTitle("All Patients")
                                    .setMessage(sb.toString())
                                    .setIcon(R.drawable.clinic)
                                    .setPositiveButton("OK", null)
                                    .show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }
        });

        one_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileNum = file_number.getText().toString();
                if(fileNum.isEmpty()){
                        Toast.makeText(add_appointment.this, "the file number is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = getIntent();
                    String clinic_name = intent.getStringExtra("CLINIC_NAME");
                    if (clinic_name == null) {
                        Toast.makeText(add_appointment.this, "Clinic name not found!", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        database = FirebaseDatabase.getInstance().getReference();
                        database.child(clinic_name).child("patient").child(fileNum).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    patient_name.setText(snapshot.child("name").getValue(String.class));
                                    patient_number.setText(snapshot.child("number").getValue(String.class));
                                    session_type.setText(snapshot.child("session_type").getValue(String.class));
                                    String savedDate = snapshot.child("date").getValue(String.class);
                                    String[] dateParts = savedDate.split("/");
                                    int day = Integer.parseInt(dateParts[0]);
                                    int month = Integer.parseInt(dateParts[1]) - 1;
                                    int year = Integer.parseInt(dateParts[2]);
                                    patient_date.updateDate(year,month,day);
                                }
                                else{
                                    Toast.makeText(add_appointment.this, "file number not found!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(add_appointment.this, "error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }
        });
    }
    private void clean(){
        file_number.setText("");
        patient_name.setText("");
        patient_number.setText("");
        session_type.setText("");
        Calendar calendar = Calendar.getInstance();
        patient_date.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }




}