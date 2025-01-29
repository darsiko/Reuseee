package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailPayment extends Fragment {
    LinearLayout back;
    TextView textPrezzo, etCardNumber, etExpiryDate, etCVC;
    RadioButton rbCreditCard;
    Button btnPayNow;
    Product purchasedProduct;

    public DetailPayment() {
        // Costruttore vuoto richiesto per i Fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_payment, container, false);
        back = view.findViewById(R.id.goBackPreviusPage);
        btnPayNow = view.findViewById(R.id.btnPayNow);
        textPrezzo = view.findViewById(R.id.tvTotalAmount);
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etExpiryDate = view.findViewById(R.id.etExpiryDate);
        etCVC = view.findViewById(R.id.etCVC);
        rbCreditCard = view.findViewById(R.id.rbCreditCard);
        Bundle args = getArguments();
        if (args != null) {
            String prodId = args.getString("prodId");
            String prezzo = args.getString("prezzo");
            String nome = args.getString("nome");
            String idVenditore = args.getString("sellerId");
            String descrizione = args.getString("descrizione");
            String imgUrl = args.getString("imageProd");
            Boolean isBaratto = args.getBoolean("baratto");
            String idOrdine = args.getString("idOrdine");
            textPrezzo.setText(prezzo+"€");
            double price = 0;
            try {
                price = Double.parseDouble(prezzo);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            }
            purchasedProduct = new Product(prodId ,idVenditore, nome, descrizione, price, isBaratto, imgUrl, idOrdine );
        }
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields()) {
                    return; // Stop the payment process if validation fails
                }

                // Create a Product object for the purchased item

                HomeScreen homeScreen = new HomeScreen();

                onDeleteProductClick(purchasedProduct);
                // Perform the fragment transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, homeScreen);
                transaction.addToBackStack(null); // Add to back stack if you want to go back to this fragment
                transaction.commit();

                Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        // Collega il layout XML al Fragment
        return view;
    }
    private boolean validateFields() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();
        String cvc = etCVC.getText().toString().trim();

        if (rbCreditCard.isChecked()) { // Only validate if Credit Card is selected
            if (cardNumber.length() != 16) {
                etCardNumber.setError("Enter a valid 16-digit card number");
                etCardNumber.requestFocus();
                return false;
            }

            // Modified validation: MMYY format without the slash
            if (!expiryDate.matches("(0[1-9]|1[0-2])\\d{2}")) { // MMYY format
                etExpiryDate.setError("Enter a valid expiry date (MMYY)");
                etExpiryDate.requestFocus();
                return false;
            }

            if (cvc.length() != 3) {
                etCVC.setError("Enter a valid 3-digit CVC");
                etCVC.requestFocus();
                return false;
            }
        }

        return true; // All fields are valid
    }
    public void onDeleteProductClick(Product product){
        String a=product.getId();
        System.out.println("ohno!"+a);
        if(product.getId()=="" || product.getId()==null){
            Toast.makeText(getContext(), "Failed to find product", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference productRef= FirebaseDatabase.getInstance().getReference("Products").child(product.getId());
            productRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DatabaseReference userProductRef=FirebaseDatabase.getInstance().getReference("Users").child(product.getIdVenditore()).child("productsForSale").child(product.getId());
                    userProductRef.removeValue().addOnSuccessListener(unused -> {
                                // Operazione riuscita
                                System.out.println("Prodotto eliminato con successo!");
                            })
                            .addOnFailureListener(e -> {
                                // Operazione fallita
                                System.out.println("Errore durante l'eliminazione del prodotto: " + e.getMessage());
                            });
                } else {
                    System.err.println("Errore nell'eliminazione del prodotto: " + task.getException());
                }
            });
        }
    }

}