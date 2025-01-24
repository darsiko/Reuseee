package com.example.reuse.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.IOException;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;



public class EditProfileScreen extends Fragment {
    public String URL;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser authUser = auth.getCurrentUser();
    private static final int PICK_IMAGE_REQUEST = 1;
    private ShapeableImageView imageProfile;
    private ImageView imageButtonProfile;
    private Button saveEditProfile;
    private Uri imageUri;
    private Bitmap previewBitmap;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_screen, container, false);

        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText telefonoInput = view.findViewById(R.id.telefono_input);
        EditText dateEditText = view.findViewById(R.id.dateEditText);

        // Usa un ShapeableImageView per gestire l'immagine del profilo
        imageProfile = view.findViewById(R.id.image_profile);
        imageButtonProfile = view.findViewById(R.id.image_button_profile);
        saveEditProfile = view.findViewById(R.id.save_edit_profile);

        // Listener per cambiare immagine profilo
        imageButtonProfile.setOnClickListener(v -> openImageChooser());

        // Listener per salvare il profilo
        saveEditProfile.setOnClickListener(v -> saveProfileData(view));

        LinearLayout backtoProfileButton = view.findViewById(R.id.goBackProfile);
        backtoProfileButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // Recupero dati utente da Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uID = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue() != null ? snapshot.child("nome").getValue().toString() : "";
                        String cognome = snapshot.child("cognome").getValue() != null ? snapshot.child("cognome").getValue().toString() : "";
                        String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                        String country = snapshot.child("stato").getValue() != null ? snapshot.child("stato").getValue().toString() : "";
                        String city = snapshot.child("citta").getValue() != null ? snapshot.child("citta").getValue().toString() : "";
                        String cap = snapshot.child("cap").getValue() != null ? snapshot.child("cap").getValue().toString() : "";
                        String indirizzo = snapshot.child("indirizzo").getValue() != null ? snapshot.child("indirizzo").getValue().toString() : "";
                        String telefono = snapshot.child("telefono").getValue() != null ? snapshot.child("telefono").getValue().toString() : "";
                        String date = snapshot.child("data").getValue() != null ? snapshot.child("data").getValue().toString() : "";

                        // Carica i dati negli EditText
                        nomeInput.setHint(nome);
                        cognomeInput.setHint(cognome);
                        userNameInput.setHint(username);
                        countryInput.setHint(country);
                        cityInput.setHint(city);
                        capInput.setHint(cap);
                        indirizzoInput.setHint(indirizzo);
                        telefonoInput.setHint(telefono);
                        dateEditText.setHint(date);

                        // Carica l'immagine usando Picasso
                        String imageP = snapshot.child("imageUrl").getValue(String.class);
                        if (imageP != null && !imageP.isEmpty()) {
                            Picasso.get()
                                    .load(imageP)
                                    .placeholder(R.drawable.user)
                                    .into(imageProfile);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gestione errore
                }
            });
        }

        return view;
    }


    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile_screen, container, false);


        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText telefonoInput = view.findViewById(R.id.telefono_input);
        EditText dateEditText = view.findViewById(R.id.dateEditText);
        EditText imageURL = view.findViewById(R.id.image_profile);

        imageProfile = view.findViewById(R.id.image_profile);
        imageButtonProfile = view.findViewById(R.id.image_button_profile);
        saveEditProfile = view.findViewById(R.id.save_edit_profile);

        // Listener per cambiare immagine profilo
        imageButtonProfile.setOnClickListener(v -> openImageChooser());

        // Listener per salvare il profilo

        saveEditProfile.setOnClickListener(v -> saveProfileData(view));

        LinearLayout backtoProfileButton = view.findViewById(R.id.goBackProfile);
        backtoProfileButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) {
            String uID = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue() != null ? snapshot.child("nome").getValue().toString() : "";
                        String cognome = snapshot.child("cognome").getValue() != null ? snapshot.child("cognome").getValue().toString() : "";
                        String username = snapshot.child("username").getValue() != null ? snapshot.child("username").getValue().toString() : "";
                        String country = snapshot.child("stato").getValue() != null ? snapshot.child("stato").getValue().toString() : "";
                        String city = snapshot.child("citta").getValue() != null ? snapshot.child("citta").getValue().toString() : "";
                        String cap = snapshot.child("cap").getValue() != null ? snapshot.child("cap").getValue().toString() : "";
                        String indirizzo = snapshot.child("indirizzo").getValue() != null ? snapshot.child("indirizzo").getValue().toString() : "";
                        String telefono = snapshot.child("telefono").getValue() != null ? snapshot.child("telefono").getValue().toString() : "";
                        String date = snapshot.child("data").getValue() != null ? snapshot.child("data").getValue().toString() : "";
                        String imageUrl = snapshot.child("ImageUrl").getValue() != null ? snapshot.child("ImageUrl").getValue().toString() : "";


                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String imageP = FirebaseDatabase.getInstance().getReference().child(userId).child("imageUrl").get().toString();


                        nomeInput.setHint(nome);
                        cognomeInput.setHint(cognome);
                        userNameInput.setHint(username);
                        countryInput.setHint(country);
                        cityInput.setHint(city);
                        capInput.setHint(cap);
                        indirizzoInput.setHint(indirizzo);
                        telefonoInput.setHint(telefono);
                        dateEditText.setHint(date);



                            Picasso.get()
                                    .load(imageP)
                                    .placeholder(R.drawable.user)
                                    .into(imageProfile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }

        return view;
    }*/

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona un'immagine"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                previewBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

                // Mostra un'anteprima e richiede conferma
                showImagePreviewDialog();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Errore durante il caricamento dell'immagine", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImagePreviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Anteprima immagine");

        // Creare una vista personalizzata con l'immagine
        ImageView previewImageView = new ImageView(getContext());
        previewImageView.setImageBitmap(previewBitmap);
        previewImageView.setAdjustViewBounds(true);
        previewImageView.setPadding(20, 20, 20, 20);

        builder.setView(previewImageView);

        builder.setPositiveButton("Conferma", (dialog, which) -> {
            // Imposta l'immagine selezionata come immagine profilo
            imageProfile.setImageBitmap(previewBitmap);
            Toast.makeText(getContext(), "Immagine aggiornata", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> {
            // Non fare nulla
            dialog.dismiss();
        });

        builder.show();
    }



    private void saveProfileData(View view) {

        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText dataInput = view.findViewById(R.id.dateEditText);
        EditText telefonoInput = view.findViewById(R.id.telefono_input);

        String nome = nomeInput.getText().toString();
        String cognome = cognomeInput.getText().toString();
        String username = userNameInput.getText().toString();
        String country = countryInput.getText().toString();
        String city = cityInput.getText().toString();
        String cap = capInput.getText().toString();
        String indirrizo = indirizzoInput.getText().toString();
        String data = dataInput.getText().toString();
        String telefono = telefonoInput.getText().toString();

        if (imageUri != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/" + userId);

            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Wait for the URL to be obtained
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String URL = uri.toString();

                            // Now perform the update with the obtained URL
                            updateUserData(nome, cognome, username, country, city, cap, indirrizo, data, telefono, URL);
                        }).addOnFailureListener(e -> {
                            // Handle any failure to get the download URL
                            Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any failure to upload the file
                        Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // No image to upload; proceed with data saving
            updateUserData(nome, cognome, username, country, city, cap, indirrizo, data, telefono, null);
        }
    }

    private void updateUserData(String nome, String cognome, String username, String country, String city,
                                String cap, String indirrizo, String data, String telefono, String URL) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uID = currentUser.getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

            Map<String, Object> updates = new HashMap<>();
            if (!nome.isEmpty()) updates.put("nome", nome);
            if (!cognome.isEmpty()) updates.put("cognome", cognome);
            if (!username.isEmpty()) updates.put("username", username);
            if (!country.isEmpty()) updates.put("stato", country);
            if (!city.isEmpty()) updates.put("citta", city);
            if (!cap.isEmpty()) updates.put("cap", cap);
            if (!indirrizo.isEmpty()) updates.put("indirizzo", indirrizo);
            if (!telefono.isEmpty()) updates.put("telefono", telefono);
            if (!data.isEmpty()) updates.put("data", data);
            if (URL != null && !URL.isEmpty()) updates.put("imageUrl", URL);

            dbRef.updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                    });
        }
    }



    /*private void saveProfileData(View view) {

        if (imageUri != null) {
            String userId = auth.getCurrentUser().getUid();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/" + userId);

            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Ottieni l'URL del file caricato
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            URL = uri.toString();

                        });
                    });
        }


        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText cognomeInput = view.findViewById(R.id.surname_input);
        EditText userNameInput = view.findViewById(R.id.Username_input);
        EditText countryInput = view.findViewById(R.id.country_input);
        EditText cityInput = view.findViewById(R.id.city_input);
        EditText capInput = view.findViewById(R.id.cap_input);
        EditText indirizzoInput = view.findViewById(R.id.indirizzo_input);
        EditText dataInput = view.findViewById(R.id.dateEditText);
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
        String data = dataInput.getText().toString();
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
            if(!data.isEmpty()) updates.put("data", data);
            if(!URL.isEmpty()) updates.put("imageUrl", URL);

            dbRef.updateChildren(updates);
        }



        Toast.makeText(getContext(), "ENSDRONGOOO", Toast.LENGTH_SHORT).show();
    }*/
}


