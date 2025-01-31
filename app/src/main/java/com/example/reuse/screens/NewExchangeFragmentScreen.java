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
import com.example.reuse.models.Product;
import com.example.reuse.models.Scambio;
import com.example.reuse.models.User;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class NewExchangeFragmentScreen extends Fragment implements ProductsUtente1ExchangeAdapter.OnItemClickListener {

    private ProductsUtente1ExchangeAdapter adapterUtente1;
    private ProductsUtente2ExchangeAdapter adapterUtente2;
    Scambio scambio;
    Chat chat;
    TextView utente1, utente2;
    EditText offerente, ricevente;
    Button addUtente1, addUtente2, annulla, conferma;
    RecyclerView utente1Lista, utente2Lista;
    List<Product> productListutente1 = new ArrayList<>(),
            productListutente2 = new ArrayList<>(),
            newProductListUtente1,
            newProductListUtente2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_screen, container, false);

        conferma = view.findViewById(R.id.conferma);
        offerente = view.findViewById(R.id.editTextOfferente);
        ricevente = view.findViewById(R.id.editTextRicevente);

        utente1 = view.findViewById(R.id.venditore);
        utente2 = view.findViewById(R.id.acquirente);
        // Initialize RecyclerViews
        utente1Lista = view.findViewById(R.id.utente1Lista);
        utente2Lista = view.findViewById(R.id.utente2Lista);
        utente1Lista.setLayoutManager(new LinearLayoutManager(getContext()));
        utente2Lista.setLayoutManager(new LinearLayoutManager(getContext()));
        annulla = view.findViewById(R.id.annullaaaa);
        // **Ensure lists are empty at the beginning**
        productListutente1 = new ArrayList<>();
        productListutente2 = new ArrayList<>();
        newProductListUtente1 = new ArrayList<>();
        newProductListUtente2 = new ArrayList<>();
        Bundle b = getArguments();
        scambio = new Scambio(FirebaseAuth.getInstance().getUid());
        String chatId = b.getString("chatId");
        chat = new Chat(chatId);
        chat.uploadChat();
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
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        new User(sellerIdS, new User.UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                utente1.setText(user.getUsername());
                productListutente1.clear(); // Ensure list is cleared before adding new data
                for (String s : user.getProductsForSale()) {
                    productListutente1.add(new Product(s));
                }
                adapterUtente1.updateList(new ArrayList<>()); // Keep empty initially
            }

            @Override
            public void onError(Exception e) {
                // Handle errors
            }
        });
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String offerenteText = offerente.getText().toString().trim();
                String riceventeText = ricevente.getText().toString().trim();

                if (offerenteText.isEmpty() || riceventeText.isEmpty()) {
                    Toast.makeText(getContext(), "Inserisci un importo valido", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double soldiOfferente = Double.parseDouble(offerenteText);
                    double soldiRicevente = Double.parseDouble(riceventeText);

                    for (Product p : newProductListUtente1) {
                        scambio.addListaOfferente(p.getId());
                    }
                    for (Product p : newProductListUtente2) {
                        scambio.addListaRicevente(p.getId());
                    }

                    scambio.setSoldiOfferente(soldiOfferente);
                    scambio.setSoldiRicevente(soldiRicevente);

                    System.out.println("idchat: " + chat.getId());

                    chat.uploadScambio(scambio);
                    ChatScreen chatScreen = new ChatScreen();
                    Bundle b = new Bundle();
                    b.putString("sellerId", sellerIdS);
                    chatScreen.setArguments(b);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, chatScreen);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Inserisci un numero valido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatScreen chatScreen = new ChatScreen();

                Bundle b = new Bundle();
                b.putString("sellerId", sellerIdS);
                chatScreen.setArguments(b);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, chatScreen);
                transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                transaction.commit();
            }
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
            public void onError(Exception e) {
                // Handle errors
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
    public void onItemClick(Product product) {
        // Handle item click, if necessary
    }
}
