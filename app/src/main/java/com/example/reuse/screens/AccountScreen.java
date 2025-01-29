package com.example.reuse.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reuse.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AccountScreen extends Fragment {
    Button broughtProducts;
    private ShapeableImageView imageProfile;

    private LinearLayout logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_screen, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Find the Button after the layout has been inflated
        LinearLayout editProfileButton = view.findViewById(R.id.editProfile);
        broughtProducts = (Button) view.findViewById(R.id.productBrought);


        logout = (LinearLayout) view.findViewById(R.id.logOut);

        TextView nominativo = view.findViewById(R.id.NomeECognome);
        TextView fieldName = view.findViewById(R.id.fieldName);
        TextView fieldEmail = view.findViewById(R.id.fieldEmail);
        TextView fieldDate = view.findViewById(R.id.birthDate);
        TextView location = view.findViewById(R.id.location);
        TextView fieldState = view.findViewById(R.id.fieldState);

        imageProfile = view.findViewById(R.id.imageProfile);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(currentUser!=null){
            String uID = currentUser.getUid();
            if(currentUser.isAnonymous()){
                editProfileButton.setVisibility(View.GONE);
                broughtProducts.setVisibility(View.GONE);
            }
            else{
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

                dbRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nome = snapshot.child("nome").getValue(String.class);
                            String cognome = snapshot.child("cognome").getValue(String.class);
                            String email = auth.getCurrentUser().getEmail();
                            String date = snapshot.child("data").getValue(String.class);
                            String city = snapshot.child("citta").getValue(String.class);
                            String state = snapshot.child("stato").getValue(String.class);

                            String imageP = snapshot.child("imageUrl").getValue(String.class);
                            if (imageP != null && !imageP.isEmpty()) {
                                Picasso.get()
                                        .load(imageP)
                                        .placeholder(R.drawable.user)
                                        .into(imageProfile);
                            }



                            String newName = nome+" "+cognome;
                            String newLocation = city+", "+state;

                            nominativo.setText(newName);
                            fieldName.setText(newName);
                            fieldEmail.setText(email);
                            fieldDate.setText(date);
                            location.setText(newLocation);
                            fieldState.setText(state);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() != null && auth.getCurrentUser().isAnonymous()) {
                    auth.getCurrentUser().delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            System.out.println("Account anonimo eliminato.");
                        } else {
                            System.err.println("Errore durante l'eliminazione dell'account anonimo.");
                        }
                    });
                }
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileScreen editProfileFragment = new EditProfileScreen();

                // Perform the fragment transaction to replace the current fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editProfileFragment)  // Use the correct container ID
                        .addToBackStack(null)  // Optional: add to the back stack to enable back navigation
                        .commit();
            }
        });

        broughtProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductBoughtScreen productBoughtScreen = new ProductBoughtScreen();

                // Perform the fragment transaction to replace the current fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, productBoughtScreen)  // Use the correct container ID
                        .addToBackStack(null)  // Optional: add to the back stack to enable back navigation
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}