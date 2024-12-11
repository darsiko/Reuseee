package com.example.reuse.screens;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;

import java.util.Calendar;
import java.util.List;


public class EditProductScreen extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product_screen, container, false);
        EditText nameProd = view.findViewById(R.id.name_input);
        EditText priceProd = view.findViewById(R.id.prezzo);
        ImageView file = view.findViewById(R.id.product_image);
        ImageButton addTag = view.findViewById(R.id.add_tag);
        // Back button functionality
        LinearLayout backtoProfileButton = view.findViewById(R.id.goBackPreviusPage);
        backtoProfileButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
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
            String price = args.getString("price", "");
            Integer imageProd = args.getInt("imageResId");
            List<String> tagList = args.getStringArrayList("tags");

            nameProd.setText(nomeProd);
            priceProd.setText(price);
            file.setImageResource(imageProd);

            if (tagList != null) {
                for (String tag : tagList) {
                    addTagToLayout(view, tag); // Pass the view to the method
                }
            }

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
}