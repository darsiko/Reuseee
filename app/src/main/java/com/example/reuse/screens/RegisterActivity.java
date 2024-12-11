package com.example.reuse.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.HashMap;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reuse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button signUp;
    Button goBack;

    private EditText email;
    private EditText password;
    private EditText nome;
    private EditText cognome;
    private EditText username;
    private EditText cap;
    private EditText indirizzo;
    private FirebaseAuth auth;

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
        cap=findViewById(R.id.cap_input);
        indirizzo=findViewById(R.id.indirizzo_input);
        EditText dateEditText = findViewById(R.id.dateEditText);
        auth= FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                String txt_username=username.getText().toString();
                String txt_nome=username.getText().toString();
                String txt_cognome=username.getText().toString();
                Integer txt_cap=Integer.parseInt(username.getText().toString());
                String txt_indirizzo=username.getText().toString();
                String txt_date=dateEditText.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(RegisterActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if(txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {

                    /*HashMap<String, Object> map = new HashMap<>();
                    map.put("Username", username);
                    map.put("Nome", nome);
                    map.put("Cognome", cognome);
                    map.put("CAP", cap);
                    map.put("Indirizzo", indirizzo);
                    map.put("Data di nascita", date);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).updateChildren(map);*/
                    registerUser(txt_email, txt_password);
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

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Register user successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomePage.class));
                }else{
                    Toast.makeText(RegisterActivity.this,"Register failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}