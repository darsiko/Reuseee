package com.example.reuse.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.HashMap;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reuse.R;
import com.example.reuse.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.net.Uri;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import java.util.UUID;
import com.bumptech.glide.Glide;


public class RegisterActivity extends AppCompatActivity {
    Button signUp;
    Button goBack;

    private EditText email;
    private EditText password;
    private EditText nome;
    private EditText cognome;
    private EditText telefono;
    private EditText username;
    private EditText stato;
    private EditText citta;
    private EditText cap;
    private EditText indirizzo;
    private String imageUrl;

    private FirebaseAuth auth;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        signUp = (Button) findViewById(R.id.sign_up);
        goBack = (Button) findViewById(R.id.go_back);

        username=findViewById(R.id.username_input);
        email=findViewById(R.id.email_input);
        password=findViewById(R.id.password_input);
        nome=findViewById(R.id.nome_input);
        cognome=findViewById(R.id.cognome_input);
        telefono=findViewById(R.id.numero_input);
        stato=findViewById(R.id.stato_input);
        citta=findViewById(R.id.citta_input);
        indirizzo=findViewById(R.id.indirizzo_input);
        cap=findViewById(R.id.cap_input);
        EditText dateEditText = findViewById(R.id.dateEditText);
        auth= FirebaseAuth.getInstance();

        FirebaseStorage storage = FirebaseStorage.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                String txt_username=username.getText().toString();
                String txt_nome=nome.getText().toString();
                String txt_cognome=cognome.getText().toString();
                String txt_telefono=telefono.getText().toString();
                String txt_indirizzo=indirizzo.getText().toString();
                String txt_stato=stato.getText().toString();
                String txt_citta=citta.getText().toString();
                Integer txt_cap=Integer.parseInt(cap.getText().toString());
                String txt_date=dateEditText.getText().toString();


                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_stato) || TextUtils.isEmpty(txt_citta) || TextUtils.isEmpty(txt_date) || TextUtils.isEmpty(txt_indirizzo) || TextUtils.isEmpty(txt_cognome) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_nome)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password, txt_username, txt_nome, txt_cognome, txt_telefono, txt_stato, txt_citta, txt_cap, txt_indirizzo, txt_date, String.valueOf(R.drawable.user));
                }
            }
        });



        dateEditText.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Mostra il DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RegisterActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        dateEditText.setText(selectedDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void registerUser(String email, String password, String username, String nome, String cognome, String telefono, String stato, String citta, Integer cap, String indirizzo, String date, String imageUrl) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Register user successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomePage.class));

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    User user=new User(username, nome, cognome, telefono, stato, citta, cap, indirizzo, date);
                    databaseReference.child(uid).setValue(user);
                }else{
                    Toast.makeText(RegisterActivity.this,"Register failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}