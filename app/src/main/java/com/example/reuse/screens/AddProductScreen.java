package com.example.reuse.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddProductScreen extends Fragment {
    private EditText nome;
    private EditText prezzo;
    private Switch baratto;
    private EditText descrizione;
    private LinearLayout goBack;
    private Button aggiungi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        nome=view.findViewById(R.id.name_input);
        prezzo=view.findViewById(R.id.prezzo_input);
        baratto=view.findViewById(R.id.baratto);
        descrizione=view.findViewById(R.id.descrizione_input);

        aggiungi = (Button) view.findViewById(R.id.aggiungi);

        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_nome=nome.getText().toString();
                String txt_prezzo=prezzo.getText().toString();
                boolean txt_baratto=baratto.isChecked();
                String txt_descrizione=descrizione.getText().toString();
                if(TextUtils.isEmpty(txt_nome) || TextUtils.isEmpty(txt_descrizione) || TextUtils.isEmpty(txt_prezzo)){
                    Toast.makeText(getContext(), "Empty credentials", Toast.LENGTH_SHORT).show();
                }else{
                    AggiungiProdotto(txt_nome, txt_prezzo, txt_baratto, txt_descrizione);
                }
            }
        });


        goBack = (LinearLayout) view.findViewById(R.id.goBackPreviusPage);
        ImageButton tagButton = view.findViewById(R.id.add_tag);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the dialog to add a tag
                openTagDialog();
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void AggiungiProdotto(String nome, String prezzo, boolean baratto, String descrizione) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Products");
        String productId = databaseRef.push().getKey();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Utente non autenticato", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid = currentUser.getUid();
        double pr = 0;
        try {
            pr = Double.parseDouble(prezzo);
            if (pr <= 0) {
                Toast.makeText(getContext(), "Il prezzo deve essere maggiore di zero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Formato prezzo non valido", Toast.LENGTH_SHORT).show();
            return;
        }

        Product prodotto= new Product(uid, nome, descrizione, pr, baratto);
        databaseRef.child(productId).setValue(prodotto).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "aggiunto", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Errore nell'aggiunta del prodotto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openTagDialog() {
        // Create an EditText for input
        final EditText tagInput = new EditText(getContext());
        tagInput.setHint("Enter a tag");

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Tag")
                .setView(tagInput)  // Set the EditText view
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the tag entered by the user
                        String tag = tagInput.getText().toString();
                        if (!tag.isEmpty()) {
                            // Add tag to the layout
                            addTagToLayout(tag);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)  // Close the dialog
                .show();
    }

    private void addTagToLayout(String tag) {
        // Get the container to add the tags dynamically
        // Get the container to add the tags dynamically
        LinearLayout tagsContainer = getView() != null ? getView().findViewById(R.id.containerTags) : null;
        if (tagsContainer == null) {
            Toast.makeText(getContext(), "Errore nel layout dei tag", Toast.LENGTH_SHORT).show();
            return;
        }


        // Create a new TextView for the tag
        TextView tagTextView = new TextView(getContext());
        tagTextView.setText(tag);
        tagTextView.setBackgroundResource(R.drawable.tag_background); // A drawable for background (optional)
        tagTextView.setPadding(10, 5, 10, 5);
        tagTextView.setTextColor(Color.WHITE); // Text color for the tag

        // Create LayoutParams for the TextView with margins
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set the marginStart using LayoutParams
        params.setMarginStart(10);  // Adjust the margin value here
        params.topMargin = 10; // Optional, if you want top margin between tags

        // Apply the LayoutParams to the TextView
        tagTextView.setLayoutParams(params);

        // Add the new TextView to the container
        tagsContainer.addView(tagTextView);
    }
}