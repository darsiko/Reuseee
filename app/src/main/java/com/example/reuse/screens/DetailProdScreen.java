package com.example.reuse.screens;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.reuse.R;
import com.google.firebase.auth.FirebaseAuth;

public class DetailProdScreen extends Fragment {

    private TextView nomeProdotto, prezzoProdotto, nomeVenditore, descrizione, statusVenditore;
    private ImageView imageProd, sellerImage;
    private Button compra, scambia, contatta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_prod_screen, container, false);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize UI components
        nomeProdotto = view.findViewById(R.id.productName);
        prezzoProdotto = view.findViewById(R.id.productPrice);
        imageProd = view.findViewById(R.id.productImage);
        nomeVenditore = view.findViewById(R.id.sellerName);
        sellerImage = view.findViewById(R.id.sellerImage);
        descrizione = view.findViewById(R.id.descrizione);
        compra = view.findViewById(R.id.buyButton);
        scambia = view.findViewById(R.id.tradeButton);
        contatta = view.findViewById(R.id.contactButton);
        statusVenditore = view.findViewById(R.id.sellerProductsCount);
        LinearLayout linearLayout = view.findViewById(R.id.goBackPreviusPage);

        // Restore data from bundle

        Bundle args = getArguments();
        if (args != null) {

            String sellerId = args.getString("sellerId", "");
            String nameProd = args.getString("nome");
            String prezzoProd = args.getString("prezzo");
            String descriz = args.getString("descrizione");
            String venditore = args.getString("venditore");
            String imageProdUrl = args.getString("imageProd");
            String prodottiVenditore = args.getString("status");
            String immagineVenditore = args.getString("userProfileImage");
            if(currentUserId.equals(sellerId)){
                contatta.setVisibility(View.GONE);

                scambia.setVisibility(View.GONE);

                compra.setVisibility(View.GONE);
            }

            nomeProdotto.setText(nameProd);
            prezzoProdotto.setText(prezzoProd);
            nomeVenditore.setText(venditore);
            Glide.with(requireContext())
                    .load(immagineVenditore)
                    .placeholder(R.drawable.user) // Optional: placeholder image while loading
                    .into(sellerImage);
            Glide.with(requireContext()).load(imageProdUrl).into(imageProd);
            statusVenditore.setText(prodottiVenditore);
            descrizione.setText(descriz);
        }


        linearLayout.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        return view;
    }
}
