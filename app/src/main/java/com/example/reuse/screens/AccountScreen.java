package com.example.reuse.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reuse.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountScreen extends Fragment {
    Button broughtProducts, myProducts;

    private LinearLayout logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_screen, container, false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Find the Button after the layout has been inflated
        LinearLayout editProfileButton = view.findViewById(R.id.editProfile);
        broughtProducts = (Button) view.findViewById(R.id.productBrought);
        myProducts = (Button) view.findViewById(R.id.myProducts);

        logout = (LinearLayout) view.findViewById(R.id.logOut);

        if (currentUser != null && currentUser.isAnonymous()) {
            editProfileButton.setVisibility(View.GONE);
            broughtProducts.setVisibility(View.GONE);
            myProducts.setVisibility(View.GONE);
        }

        FirebaseAuth auth;
        auth= FirebaseAuth.getInstance();
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
        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyProductsScreen myProductsScreen = new MyProductsScreen();

                // Perform the fragment transaction to replace the current fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myProductsScreen)  // Use the correct container ID
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