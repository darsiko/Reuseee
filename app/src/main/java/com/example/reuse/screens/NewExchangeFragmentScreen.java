package com.example.reuse.screens;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reuse.R;
import com.example.reuse.adapter.ProductsUtente1ExchangeAdapter;
import com.example.reuse.adapter.ProductsUtente2ExchangeAdapter;
import com.example.reuse.models.Chat;
import com.example.reuse.models.Messaggio;
import com.example.reuse.models.Product;
import com.example.reuse.models.Scambio;
import com.example.reuse.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewExchangeFragmentScreen extends Fragment implements ProductsUtente1ExchangeAdapter.OnItemClickListener {
    private ProductsUtente1ExchangeAdapter adapterUtente1;
    private ProductsUtente2ExchangeAdapter adapterUtente2;
    Button addUtente1, addUtente2, annulla, conferma;
    TextView utente1, utente2;
    RecyclerView utente1Lista, utente2Lista;
    List<Product> productListutente1 = new ArrayList<>(),
            productListutente2 = new ArrayList<>(),
            newProductListUtente1 = new ArrayList<>(),
            newProductListUtente2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_screen, container, false);

        // Initialize RecyclerViews
        utente1Lista = view.findViewById(R.id.utente1Lista);
        utente2Lista = view.findViewById(R.id.utente2Lista);
        utente1Lista.setLayoutManager(new LinearLayoutManager(getContext()));
        utente2Lista.setLayoutManager(new LinearLayoutManager(getContext()));

        utente1 = view.findViewById(R.id.venditore);
        utente2 = view.findViewById(R.id.acquirente);

        conferma = view.findViewById(R.id.confermaa);

        //QUI INIZIA IL PROBLEMA, MA DOV'è DI PRECISO??
        EditText offerente, ricevente;
        offerente = view.findViewById(R.id.editTextOfferente);
        ricevente = view.findViewById(R.id.editTextRicevente);
        String offer = offerente.getText().toString();
        String recive = ricevente.getText().toString();
        offerente.setText(offer+"€");
        ricevente.setText(recive+"€");

        annulla = view.findViewById(R.id.annullaaaa);

        // **Ensure lists are empty at the beginning**
        productListutente1 = new ArrayList<>();
        productListutente2 = new ArrayList<>();
        newProductListUtente1 = new ArrayList<>();
        newProductListUtente2 = new ArrayList<>();

        // Initialize adapters with empty lists
        adapterUtente1 = new ProductsUtente1ExchangeAdapter(getContext(), new ArrayList<>(), this);
        utente1Lista.setAdapter(adapterUtente1);
        adapterUtente2 = new ProductsUtente2ExchangeAdapter(getContext(), new ArrayList<>(), this);
        utente2Lista.setAdapter(adapterUtente2);
        addUtente1 = view.findViewById(R.id.buttonAddutente1);
        addUtente2 = view.findViewById(R.id.buttonAddutente2);
        // Fetch and update seller products
        Bundle bundle = getArguments();
        assert bundle != null;
        String sellerIdS = bundle.getString("sellerId");
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        new User(sellerIdS, new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                String chatId = bundle.getString("chatId");
                assert chatId != null;
                DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId).child("scambio");
                chatRef.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().exists()){
                        Double cashOff = Objects.requireNonNull(task.getResult().child("soldiOfferente").getValue(Double.class));
                        Double cashRec = Objects.requireNonNull(task.getResult().child("soldiRicevente").getValue(Double.class));
                        offerente.setHint(cashOff.toString()+"€");
                        ricevente.setHint(cashRec.toString()+"€");
                    }
                });
                utente1.setText(user.getUsername());
                productListutente1.clear(); // Ensure list is cleared before adding new data
                for (String s : user.getProductsForSale()) {
                    productListutente1.add(new Product(s));
                }
                adapterUtente1.updateList(new ArrayList<>()); // Keep empty initially
            }

            @Override
            public void onError(Exception e) {}
        });

        addUtente1.setOnClickListener(v ->
                showUtente1ProductsDialog("Utente 1", productListutente1, adapterUtente1));

        addUtente2.setOnClickListener(v ->
                showutente2ProductDialog("Utente 2", productListutente2, adapterUtente2));

        new User(currentUserId, new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                utente2.setText(user.getUsername());
                productListutente2.clear(); // Ensure list is cleared before adding new data
                for (String s : user.getProductsForSale()) {
                    productListutente2.add(new Product(s));
                }
                adapterUtente2.updateList(new ArrayList<>()); // Keep empty initially
            }
            @Override
            public void onError(Exception e) {}
        });

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatScreen chatScreen = new ChatScreen();
                Bundle b = new Bundle();
                b.putString("seller", bundle.getString("sellerUsername"));

                chatScreen.setArguments(b);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, chatScreen);
                transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                transaction.commit();
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cID = bundle.getString("chatId");
                if(cID!=null){
                    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(cID);
                    chatRef.get().addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult().exists()){
                            String u1 = bundle.getString("u1");
                            String u2 = bundle.getString("u2").toString();
                            assert u1 != null;
                            List<Messaggio> mex = new ArrayList<>();
                            for(DataSnapshot m : task.getResult().child("messaggi").getChildren()){
                                //gather single message info
                                String mexID = m.getKey();
                                String content = m.child("contenuto").getValue(String.class);
                                String data = m.child("dataeora").getValue(String.class);
                                String mittente = m.child("idMittente").getValue(String.class);
                                Messaggio temp = new Messaggio(mittente, data, false, content, mexID);
                                mex.add(temp);
                            }
                            String offerText = offerente.getText().toString();
                            String ricevText = ricevente.getText().toString();
                            offerText = offerText.substring(0, offerText.length()-1);
                            ricevText = ricevText.substring(0, ricevText.length()-1);

                            double o = offerText.isEmpty() ? 0 : Double.parseDouble(offerText);
                            double r = ricevText.isEmpty() ? 0 : Double.parseDouble(ricevText);
                            List<String> myProd = new ArrayList<>();
                            List<String> hisProd = new ArrayList<>();
                            for(Product p : newProductListUtente1){
                                myProd.add(p.getId());
                            }
                            for(Product p : newProductListUtente2){
                                hisProd.add(p.getId());
                            }
                            Scambio s = new Scambio(currentUserId, o, r,myProd, hisProd);
                            Chat c = new Chat(cID, u1, u2, mex, s);
                            c.uploadScambio(s);
                        }
                    });
                    ChatListScreen chatListFragment = new ChatListScreen();
                    chatListFragment.setArguments(new Bundle());
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, chatListFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return view;
    }
    private void showUtente1ProductsDialog(String userType, List<Product> productList, ProductsUtente1ExchangeAdapter adapter) {
        if (productList.isEmpty()) {
            Toast.makeText(getContext(), "Non ci sono prodotti", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a list of product names
        String[] productNames = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            productNames[i] = productList.get(i).getNome();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleziona un prodotto");
        builder.setItems(productNames, (dialog, which) -> {
            Product selectedProduct = productList.get(which);

            // Remove from the available products list
            productList.remove(which);

            // Add to the selected list
            newProductListUtente1.add(selectedProduct);

            // Update the adapter and notify changes
            adapterUtente1.updateList(newProductListUtente1);
            adapter.notifyDataSetChanged();

            // Debugging logs
            System.out.println(userType + " ha selezionato: " + selectedProduct.getNome());
            Log.d("ProductListSize", "Nuova lista: " + newProductListUtente1.size());
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    private void showutente2ProductDialog(String userType, List<Product> productList, ProductsUtente2ExchangeAdapter adapter) {
        if (productList.isEmpty()) {
            Toast.makeText(getContext(), "Non ci sono prodotti", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a list of product names
        String[] productNames = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            productNames[i] = productList.get(i).getNome();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleziona un prodotto");

        builder.setItems(productNames, (dialog, which) -> {
            Product selectedProduct = productList.get(which);

            // Remove from the available products list
            productList.remove(which);

            // Add to the selected list
            newProductListUtente2.add(selectedProduct);

            // Update the adapter and notify changes
            adapterUtente2.updateList(newProductListUtente2);
            adapter.notifyDataSetChanged();

            // Debugging logs
            System.out.println(userType + " ha selezionato: " + selectedProduct.getNome());
            Log.d("ProductListSize", "Nuova lista: " + newProductListUtente2.size());
        });

        builder.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
    @Override
    public void onItemClick(Product product) {}
}
