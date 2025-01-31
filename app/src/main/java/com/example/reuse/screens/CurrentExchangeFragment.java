package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reuse.R;
import com.example.reuse.adapter.ProductsUtente1ExchangeAdapter;
import com.example.reuse.adapter.ProductsUtente2ExchangeAdapter;
import com.example.reuse.models.Chat;
import com.example.reuse.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrentExchangeFragment extends Fragment {
    private ProductsUtente1ExchangeAdapter adapterUtente1;
    private ProductsUtente2ExchangeAdapter adapterUtente2;
    Button accetta, rifiuta;
    TextView utente1, utente2;
    RecyclerView utente1Lista, utente2Lista;
    TextView offerente, ricevente;
    Button addExchangeOfferta;
    List<Product> productListutente1 = new ArrayList<>(),
            productListutente2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_exchange, container, false);
        Bundle b = getArguments();
        assert b!=null;

        utente1Lista = view.findViewById(R.id.utente1Lista);
        utente2Lista = view.findViewById(R.id.utente2Lista);
        utente1Lista.setLayoutManager(new LinearLayoutManager(getContext()));
        utente2Lista.setLayoutManager(new LinearLayoutManager(getContext()));

        utente1 = view.findViewById(R.id.venditore);
        utente2 = view.findViewById(R.id.acquirente);

        accetta = view.findViewById(R.id.accetta);
        rifiuta = view.findViewById(R.id.rifiuta);

        offerente = view.findViewById(R.id.u1Cash);
        ricevente = view.findViewById(R.id.u2Cash);


        productListutente1 = new ArrayList<>();
        productListutente2 = new ArrayList<>();

        //gather the trade info
        String cID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String sellerID = b.getString("sellerId");
        assert sellerID != null;
        DatabaseReference cIDRef = FirebaseDatabase.getInstance().getReference("Users").child(cID);
        cIDRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().exists()){
                String cIDUsername = task.getResult().child("username").getValue(String.class);
                utente1.setText(cIDUsername);
            }
        });
        DatabaseReference sIDRef = FirebaseDatabase.getInstance().getReference("Users").child(sellerID);
        sIDRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().exists()){
                String sIDUsername = task.getResult().child("username").getValue(String.class);
                utente2.setText(sIDUsername);
            }
        });
        String chatID = b.getString("chatId");
        //Sono buyer o seller?
        assert chatID != null;
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatID).child("scambio");
        dbRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult().exists()){
                if(task.getResult().getValue()!=null){
                    String offerID = task.getResult().child("idOfferente").getValue(String.class);
                    assert offerID != null;
                    if(cID.equals(offerID)){
                        //Sono l'offerente
                        Double o = task.getResult().child("soldiOfferente").getValue(Double.class);
                        Double h = task.getResult().child("soldiRicevente").getValue(Double.class);
                        assert o!=null;
                        assert h!=null;
                        String myCash = o.toString();
                        String hisCash = h.toString();
                        offerente.setText(myCash);
                        ricevente.setText(hisCash);
                    }
                    else{
                        //Non sono l'offerente
                        Double o = task.getResult().child("soldiRicevente").getValue(Double.class);
                        Double h = task.getResult().child("soldiOfferente").getValue(Double.class);
                        assert o!=null;
                        assert h!=null;
                        String myCash = o.toString();
                        String hisCash = h.toString();
                        offerente.setText(myCash);
                        ricevente.setText(hisCash);
                    }
                }
            }
        });

        addExchangeOfferta = view.findViewById(R.id.nuovaofferta);
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