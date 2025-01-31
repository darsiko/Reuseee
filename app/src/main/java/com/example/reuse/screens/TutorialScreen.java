package com.example.reuse.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.reuse.R;

public class TutorialScreen extends Fragment {
    ImageView tutorialImagee, avatarImage;
    LinearLayout goBack;
    TextView authorTutorial, nomeTutorial, descriptionTutorial; // Add descriptionTutorial TextView
    ScrollView scrollViewDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_screen, container, false);

        tutorialImagee = view.findViewById(R.id.tutorialImage);
        avatarImage = view.findViewById(R.id.sellerImage);
        goBack = view.findViewById(R.id.goBackPreviusPage);
        authorTutorial = view.findViewById(R.id.sellerName);
        nomeTutorial = view.findViewById(R.id.tutorialName);
        scrollViewDesc = view.findViewById(R.id.descriptionn);

        // Initialize descriptionTextView (make sure it's defined in the layout)
        descriptionTutorial = view.findViewById(R.id.descriptionText);  // This should be the TextView inside the ScrollView

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String nameTutorial = args.getString("nameTutorial", "");
            Integer imageTutorial = args.getInt("imageTutorial");
            String author = args.getString("author");
            String descriptionTutorialText = args.getString("descriptionTutorial"); // Changed variable name for clarity
            Integer avatarTutorial = args.getInt("avatarTutorial");
            String urlTutorial = args.getString("url");

            tutorialImagee.setImageResource(imageTutorial);
            nomeTutorial.setText(nameTutorial);
            avatarImage.setImageResource(avatarTutorial);
            descriptionTutorial.setText(descriptionTutorialText);  // Set the description text here
            authorTutorial.setText(author);

            tutorialImagee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTutorial));
                    startActivity(intent);
                }
            });
        }

        return view;
    }
}
