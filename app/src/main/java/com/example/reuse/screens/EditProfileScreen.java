package com.example.reuse.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.reuse.R;
import com.example.reuse.models.User;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;

public class EditProfileScreen extends Fragment {
    private ImageView imageView;
    private Button saveButton;
    private Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser authUser = auth.getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_screen, container, false);

        imageView = view.findViewById(R.id.image_button_profile);
        saveButton = view.findViewById(R.id.save_edit_profile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // Back button functionality
        LinearLayout backtoProfileButton = view.findViewById(R.id.goBackProfile);
        backtoProfileButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText telefonoInput = view.findViewById(R.id.telefono_input);
        EditText dateEditText = view.findViewById(R.id.dateEditText);
        EditText emailInput = view.findViewById(R.id.email_input);

        dateEditText.setOnClickListener( x -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Mostra il DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        dateEditText.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Save button functionality
        saveButton.setOnClickListener(v -> {
            String date = dateEditText.getText().toString();
            saveProfileData(view, date);
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String uID = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        String cognome = snapshot.child("cognome").getValue(String.class);
                        String username = snapshot.child("username").getValue(String.class);
                        String country = snapshot.child("stato").getValue(String.class);
                        String city = snapshot.child("citta").getValue(String.class);
                        String cap = snapshot.child("cap").getValue(String.class);
                        String indirizzo = snapshot.child("indirizzo").getValue(String.class);
                        String telefono = snapshot.child("telefono").getValue(String.class);
                        String date = snapshot.child("data").getValue(String.class);
                        String email = authUser.getEmail();

                        nomeInput.setHint(nome);
                        cognomeInput.setHint(cognome);
                        userNameInput.setHint(username);
                        countryInput.setHint(country);
                        cityInput.setHint(city);
                        capInput.setHint(cap);
                        indirizzoInput.setHint(indirizzo);
                        telefonoInput.setHint(telefono);
                        dateEditText.setHint(date);
                        emailInput.setHint(email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }

        return view;
    }

    // Save Profile Data (Optional: you can implement saving data here)
    private void saveProfileData(View view, String Newdate) {
        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText telefonoInput = view.findViewById(R.id.telefono_input);

        EditText emailInput = view.findViewById(R.id.email_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Get data from inputs
        String nome = nomeInput.getText().toString();
        String cognome = cognomeInput.getText().toString();
        String username = userNameInput.getText().toString();
        String country = countryInput.getText().toString();
        String city = cityInput.getText().toString();
        String cap = capInput.getText().toString();
        String indirrizo = indirizzoInput.getText().toString();
        String telefono = telefonoInput.getText().toString();

        // Handle saving the data (e.g., store in SharedPreferences, send to server, etc.)
        // Example: show a toast or save data
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            String uID = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

            Map<String, Object> updates = new HashMap<>();
            if(!nome.isEmpty()) updates.put("nome", nome);
            if(!cognome.isEmpty()) updates.put("cognome", cognome);
            if(!username.isEmpty()) updates.put("username", username);
            if(!country.isEmpty()) updates.put("stato", country);
            if(!city.isEmpty()) updates.put("citta", city);
            if(!cap.isEmpty()) updates.put("cap", cap);
            if(!indirrizo.isEmpty()) updates.put("indirizzo", indirrizo);
            if(!telefono.isEmpty()) updates.put("telefono", telefono);
            if(!Newdate.isEmpty()) updates.put("data", Newdate);
            if(!email.isEmpty()){
                authUser.updateEmail(email);
                //QUI
            }
            if(!password.isEmpty()){
                authUser.updatePassword(password);
            }
            dbRef.updateChildren(updates);
        }



        Toast.makeText(getContext(), "ENSDRONGOOO", Toast.LENGTH_SHORT).show();
    }



    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // Seleziona contenuto
        startActivityForResult(Intent.createChooser(intent, "Seleziona immagine"), PICK_IMAGE_REQUEST);
        imageUri = intent.getData();
    }
}