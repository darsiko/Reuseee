package com.example.reuse.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reuse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;


public class LoginActivity extends AppCompatActivity {
    Button signUp;
    Button login;
    TextView guestLogin;
    CheckBox checkBox;
    private EditText email;
    private EditText password;
    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.email_input);
        password=findViewById(R.id.password_input);
        checkBox = findViewById(R.id.checkBoxLog);
        auth= FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();

        login = (Button) findViewById(R.id.sign_in_button);
        signUp = (Button) findViewById(R.id.sign_up_button);
        guestLogin = (TextView) findViewById(R.id.guest_login);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("rememberMe", false);
        if (isRemembered) {
            // Retrieve and set username and password
            String savedUsername = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            email.setText(savedUsername);
            password.setText(savedPassword);
            checkBox.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rememberMe", true);
                    editor.putString("email", txt_email);
                    editor.putString("password", txt_password);
                    editor.apply();
                }else {
                    // Clear saved login data if "Remember Me" is unchecked
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                }

                loginUser(txt_email, txt_password);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }

        });

        guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInAnonymously();
                if (auth.getCurrentUser() != null && auth.getCurrentUser().isAnonymous()) {
                    System.out.println("Account anonimo creato");
                    Intent i = new Intent(LoginActivity.this, HomePage.class);
                    startActivity(i);
                }else{
                    System.out.println("Errore creazione account ospite");
                }

            }
        });


    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomePage.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Esegui azioni comuni, ad esempio elimina l'account anonimo
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isAnonymous()) {
            auth.getCurrentUser().delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Account anonimo eliminato.");
                } else {
                    System.err.println("Errore durante l'eliminazione dell'account anonimo.");
                }
            });
        }
    }
}
