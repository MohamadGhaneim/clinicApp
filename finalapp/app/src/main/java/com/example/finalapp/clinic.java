package com.example.finalapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class clinic extends AppCompatActivity {
    Button join , add_clinic;
    ImageButton back;
    EditText clinic_name , password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic);

        join = findViewById(R.id.btn_join);
        clinic_name = findViewById(R.id.et_clinic_name);
        password = findViewById(R.id.et_password);
        back = findViewById(R.id.ib_back_toMain);
        add_clinic = findViewById(R.id.btn_add_my_clinic);

        add_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(clinic.this,clinic_registration.class);
                startActivity(i);

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database ;
                database = FirebaseDatabase.getInstance().getReference();
                database.child(clinic_name.getText().toString()).child("doctor").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String stored_password = snapshot.child("pass").getValue(String.class);
                            if(stored_password.equals(password.getText().toString())) {
                                Intent i = new Intent(clinic.this,add_appointment.class);
                                i.putExtra("CLINIC_NAME",clinic_name.getText().toString());
                                startActivity(i);


                            }
                            else{
                                Toast.makeText(clinic.this,"login failed",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(clinic.this,"login failed",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(clinic.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}