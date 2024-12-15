package com.example.reuse.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddProductScreen extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nome;
    private EditText prezzo;
    private Switch baratto;
    private EditText descrizione;
    private LinearLayout goBack;
    private Button aggiungi;
    private ImageButton imageButton;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        nome=view.findViewById(R.id.name_input);
        prezzo=view.findViewById(R.id.prezzo_input);
        baratto=view.findViewById(R.id.baratto);
        descrizione=view.findViewById(R.id.descrizione_input);
        imageButton =view.findViewById(R.id.productImage);
        aggiungi = view.findViewById(R.id.aggiungi);

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
                    Product product=new Product(txt_nome, txt_descrizione, Double.parseDouble(txt_prezzo), txt_baratto);
                    String pid=product.addProduct();
                    //product.uploadImage(pid, nome file uri);
                    getParentFragmentManager().popBackStack();
                }
            }
        });








        // Initialize the image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData(); // Get the URI of the selected image
                        try {
                            // Convert the URI to a Bitmap
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            // Set the Bitmap as the source of the ImageButton
                            imageButton.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Set click listener to open gallery
        imageButton.setOnClickListener(v -> openGallery());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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
    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
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