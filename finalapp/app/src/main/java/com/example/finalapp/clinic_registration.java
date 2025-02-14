package com.example.finalapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class clinic_registration extends AppCompatActivity {
    ImageButton back_to_clinic;
    Button add_clinic;
    TextView drname,drpass,drnumber , clinic_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic_registration);
        back_to_clinic = findViewById(R.id.ib_back_to_clinic);
       add_clinic = findViewById(R.id.btn_regester_clinic);
       drname = findViewById(R.id.et_dr_name);
        drpass = findViewById(R.id.et_dr_password);
        drnumber = findViewById(R.id.et_dr_number);
        clinic_name = findViewById(R.id.et_clinic_name);

        // add clinic
        add_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drname.getText().toString().trim().isEmpty() || drpass.getText().toString().trim().isEmpty() ||
                        drnumber.getText().toString().trim().isEmpty() || clinic_name.getText().toString().trim().isEmpty()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(clinic_registration.this);
                    builder.setTitle("warnnig")
                            .setMessage("all fields are required")
                            .setPositiveButton("OK", null).show();
                }else{
                    clinic_db add_info = new clinic_db();
                    DatabaseReference database ;
                    database = FirebaseDatabase.getInstance().getReference();
                    database.child(clinic_name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(clinic_registration.this ,"clinic name all ready exists" , Toast.LENGTH_LONG).show();
                            }
                            else{
                                add_info.add_clinic_info(drname.getText().toString(),drnumber.getText().toString()
                                        ,drpass.getText().toString().trim(),clinic_name.getText().toString().trim());

                                Toast.makeText(clinic_registration.this ,"success" , Toast.LENGTH_LONG).show();
                                clean();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
        //back to clinic login
        back_to_clinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back= new Intent(clinic_registration.this,clinic.class);
                startActivity(back);
                finish();

            }
        });
    }
    void clean(){
        drname.setText("");drpass.setText("");drnumber.setText(""); clinic_name.setText("");
    }

}