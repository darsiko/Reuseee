package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reuse.R;


public class DetailProdScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_prod_screen, container, false);
        TextView nomeProdotto = view.findViewById(R.id.productName);
        TextView prezzoProdotto = view.findViewById(R.id.productPrice);
        TextView nomeVenditore = view.findViewById(R.id.sellerName);
        TextView descrizione = view.findViewById(R.id.descrizione);
        TextView statusVenditore = view.findViewById(R.id.sellerProductsCount);
        LinearLayout linearLayout = view.findViewById(R.id.goBackPreviusPage);

        Bundle args = getArguments();
        if (args != null) {


            String nameProd = args.getString("nome");
            String prezzoProd = args.getString("prezzo");
            String descriz = args.getString("descrizione");
            String venditore = args.getString("venditore");
            String prodottiVenditore = args.getString("status");
            Log.d("venditore","venditore:"+ venditore);

            nomeProdotto.setText(nameProd);
            prezzoProdotto.setText(prezzoProd);
            nomeVenditore.setText(venditore);
            statusVenditore.setText(prodottiVenditore);
            descrizione.setText(descriz);
        }


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}