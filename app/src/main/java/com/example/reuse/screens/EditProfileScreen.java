package com.example.reuse.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.reuse.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import android.app.DatePickerDialog;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
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

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_screen, container, false);

        imageView = view.findViewById(R.id.image_button_profile);
        saveButton = view.findViewById(R.id.save_edit_profile);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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

        // Calendar icon functionality
        ImageView calendarIcon = view.findViewById(R.id.calendar_edit_profile);
        calendarIcon.setOnClickListener(v -> {
            showDatePickerDialog(view);
        });

        // Save button functionality
        saveButton.setOnClickListener(v -> {
            // Collect and save data from EditText fields
            saveProfileData(view);
        });

        return view;
    }

    // Show Date Picker Dialog
    private void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view1, year1, month1, dayOfMonth) -> {
            // Format and display the selected date
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            TextView dateTextView = view.findViewById(R.id.calendar_edit_text);
            dateTextView.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Save Profile Data (Optional: you can implement saving data here)
    private void saveProfileData(View view) {
        EditText nameInput = view.findViewById(R.id.name_input);
        EditText emailInput = view.findViewById(R.id.email_input);
        EditText passwordInput = view.findViewById(R.id.password_input);
        EditText countryInput = view.findViewById(R.id.country_input);

        // Get data from inputs
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String country = countryInput.getText().toString();

        // Handle saving the data (e.g., store in SharedPreferences, send to server, etc.)
        // Example: show a toast or save data
        Toast.makeText(getContext(), "Profile saved", Toast.LENGTH_SHORT).show();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // Seleziona contenuto
        startActivityForResult(Intent.createChooser(intent, "Seleziona immagine"), PICK_IMAGE_REQUEST);
        imageUri = intent.getData();
    }
}