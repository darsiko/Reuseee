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

        Bundle args = getArguments();
        if (args != null) {


            String nameProd = args.getString("nome");
            String prezzoProd = args.getString("prezzo");
            String descriz = args.getString("descrizione");
            String venditore = args.getString("venditore");
            String imageProdUrl = args.getString("imageProd");
            String prodottiVenditore = args.getString("status");

            nomeProdotto.setText(nameProd);
            prezzoProdotto.setText(prezzoProd);
            nomeVenditore.setText(venditore);
            Glide.with(requireContext()).load(imageProdUrl).into(imageProd);
            statusVenditore.setText(prodottiVenditore);
            descrizione.setText(descriz);
        }


        linearLayout.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        return view;
    }
}
