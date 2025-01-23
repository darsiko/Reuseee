package com.example.reuse.screens;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.reuse.R;

public class DetailProdScreen extends Fragment {

    private TextView nomeProdotto, prezzoProdotto, nomeVenditore, descrizione, statusVenditore;
    private ImageView imageProd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_prod_screen, container, false);

        // Initialize UI components
        nomeProdotto = view.findViewById(R.id.productName);
        prezzoProdotto = view.findViewById(R.id.productPrice);
        imageProd = view.findViewById(R.id.productImage);
        nomeVenditore = view.findViewById(R.id.sellerName);
        descrizione = view.findViewById(R.id.descrizione);
        statusVenditore = view.findViewById(R.id.sellerProductsCount);
        LinearLayout linearLayout = view.findViewById(R.id.goBackPreviusPage);

        // Restore data from bundle
        if (savedInstanceState == null && getArguments() != null) {
            nomeProdotto.setText(getArguments().getString("nome"));
            prezzoProdotto.setText(String.valueOf(getArguments().getDouble("prezzo")));
            nomeVenditore.setText(getArguments().getString("venditore"));
            imageProd.setImageURI(Uri.parse(getArguments().getString("immagine")));
            statusVenditore.setText(getArguments().getString("status"));
            descrizione.setText(getArguments().getString("descrizione"));
        }

        linearLayout.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        return view;
    }
}
