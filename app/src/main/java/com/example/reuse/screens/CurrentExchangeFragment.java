package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.reuse.R;
import com.example.reuse.models.Chat;
import com.google.firebase.auth.FirebaseAuth;

public class CurrentExchangeFragment extends Fragment {
    Button addExchangeOfferta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_exchange, container, false);

        addExchangeOfferta = view.findViewById(R.id.nuovaofferta);
        Bundle b = getArguments();
        String sellerId = b.getString("sellerId");
        Chat c = new Chat(sellerId, FirebaseAuth.getInstance().getUid());

        addExchangeOfferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewExchangeFragmentScreen newExchangeFragmentScreen = new NewExchangeFragmentScreen();
                newExchangeFragmentScreen.setArguments(b);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newExchangeFragmentScreen);
                transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                transaction.commit();
            }
        });
        return view;
    }
}