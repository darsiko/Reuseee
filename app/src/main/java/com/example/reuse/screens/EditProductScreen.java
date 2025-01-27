package com.example.reuse.screens;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.picasso.Picasso;


public class EditProductScreen extends Fragment {

    private String pid;
    private Uri imageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product_screen, container, false);
        EditText nameProd = view.findViewById(R.id.name_input);
        EditText priceProd = view.findViewById(R.id.prezzo);
        EditText descrizioneProd=view.findViewById(R.id.descrizione_input);
        ImageView file = view.findViewById(R.id.product_image);
        ImageButton addTag = view.findViewById(R.id.add_tag);
        Switch switchBaratto = view.findViewById(R.id.switch1);
        // Back button functionality
        LinearLayout backtoProfileButton = view.findViewById(R.id.goBackPreviusPage);
        backtoProfileButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        Button saveEditProduct;
        saveEditProduct=view.findViewById(R.id.save_edit_product);

        saveEditProduct.setOnClickListener(v -> {
            saveProductData(view);
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData(); // Get the URI of the selected image
                        try {
                            // Convert the URI to a Bitmap
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            // Set the Bitmap as the source of the ImageButton
                            file.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTagDialog();
            }
        });

        Bundle args = getArguments();
        if (args != null) {

            String nomeProd = args.getString("name", "");
            String price = args.getString("prezzo", "");
            boolean barattabile = args.getBoolean("barattabile");
            String descrizione = args.getString("descrizione", "");

            String productId=args.getString("id");
            String imageProd=args.getString("imageUrl");

            nameProd.setText(nomeProd);
            priceProd.setText(price);
            descrizioneProd.setText(descrizione);
            switchBaratto.setChecked(barattabile);
            Picasso.get().load(imageProd).into(file);
        }



        return view;
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
                            addTagToLayout(getView(), tag);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)  // Close the dialog
                .show();
    }
    void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void addTagToLayout(View rootView, String tag) {
        // Use the rootView passed from onCreateView
        LinearLayout tagsContainer = rootView.findViewById(R.id.containerTags);

        if (tagsContainer == null) {
            return; // Safety check to avoid NullPointerException
        }

        TextView tagTextView = new TextView(getContext());
        tagTextView.setText(tag);
        tagTextView.setBackgroundResource(R.drawable.tag_background);
        tagTextView.setPadding(10, 5, 10, 5);
        tagTextView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginStart(10);
        params.topMargin = 10;
        tagTextView.setOnClickListener(v -> {
            tagsContainer.removeView(tagTextView); // Remove the tag from the layout
        });
        tagTextView.setLayoutParams(params);
        tagsContainer.addView(tagTextView);
    }

    private void saveProductData(View view){
        EditText nomeInput = view.findViewById(R.id.name_input);
        EditText prezzoInput = view.findViewById(R.id.prezzo);
        EditText descrizioneInput = view.findViewById(R.id.descrizione_input);
        Switch switchBaratto = view.findViewById(R.id.switch1);

        String nome = nomeInput.getText().toString();
        String prezzo=prezzoInput.getText().toString();
        String descrizione=descrizioneInput.getText().toString();
        boolean baratto=switchBaratto.isChecked();



        Bundle arg = getArguments();
        if (arg != null) {
            pid=arg.getString("id");
        }

        if (imageUri != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProductImages/" + pid);

            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Wait for the URL to be obtained
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String URL = uri.toString();

                            // Now perform the update with the obtained URL
                            updateProductData(nome, prezzo, descrizione, baratto, URL, pid);
                        }).addOnFailureListener(e -> {
                            // Handle any failure to get the download URL
                            Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any failure to upload the file
                        Toast.makeText(getContext(), "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // No image to upload; proceed with data saving
            updateProductData(nome, prezzo, descrizione, baratto, null, pid);
        }
        Toast.makeText(getContext(), "Prodouct updated correctly", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }
    private void updateProductData(String nome, String prezzo, String descrizione, boolean baratto, String URL, String pid) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Products").child(pid);

        double dbPrezzo=Double.parseDouble(prezzo);

        Map<String, Object> updates = new HashMap<>();
        if (!nome.isEmpty()) updates.put("nome", nome);
        if (!descrizione.isEmpty()) updates.put("descrizione", descrizione);
        if (!prezzo.isEmpty()) updates.put("prezzo", dbPrezzo);
        if (baratto) updates.put("baratto", baratto);
        if (URL != null && !URL.isEmpty()) updates.put("imageUrl", URL);
        dbRef.updateChildren(updates);
    }
}