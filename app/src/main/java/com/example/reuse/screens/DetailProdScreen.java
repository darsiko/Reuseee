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
    private String prezzo;
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
            String prodId = args.getString("prodId");
            String sellerId = args.getString("sellerId", "");
            String nameProd = args.getString("nome");
            prezzo = args.getString("prezzo");
            String descriz = args.getString("descrizione");
            String venditore = args.getString("venditore");
            String imageProdUrl = args.getString("imageProd");
            String prodottiVenditore = args.getString("status");
            String immagineVenditore = args.getString("userProfileImage");
            String idOrdine = args.getString("idOrdine");
            if(currentUserId.equals(sellerId)){
                contatta.setVisibility(View.GONE);

                scambia.setVisibility(View.GONE);

                compra.setVisibility(View.GONE);
            }
        compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailPaymnent detailPaymnent = new DetailPaymnent();
                Bundle bundle = new Bundle();
                bundle.putString("prodId", prodId);
                bundle.putString("sellerId", sellerId);
                bundle.putString("nome", nameProd);
                bundle.putString("descrizione", descriz);
                bundle.putString("prezzo", prezzo);
                bundle.putString("imageProd", imageProdUrl);
                bundle.putString("idOrdine", idOrdine);


                detailPaymnent.setArguments(bundle);
                // Perform the fragment transaction to replace the current fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailPaymnent)  // Use the correct container ID
                        .addToBackStack(null)  // Optional: add to the back stack to enable back navigation
                        .commit();
            }
        });
            nomeProdotto.setText(nameProd);
            prezzoProdotto.setText(prezzo+"€");
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
