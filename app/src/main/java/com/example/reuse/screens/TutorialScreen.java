package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reuse.R;

import java.util.List;

public class TutorialScreen extends Fragment {
    ImageView tutorialImagee, avatarImage;
    LinearLayout goBack;
    TextView authorTutorial, nomeTutorial, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_screen, container, false);


        tutorialImagee = view.findViewById(R.id.tutorialImage);
        avatarImage = view.findViewById(R.id.sellerImage);
        goBack = view.findViewById(R.id.goBackPreviusPage);
        authorTutorial = view.findViewById(R.id.sellerName);
        nomeTutorial = view.findViewById(R.id.tutorialName);
        description = view.findViewById(R.id.description);

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
            String descriptionTutorial = args.getString("descriptionTutorial");
            Integer avatarTutorial = args.getInt("avatarTutorial");

            tutorialImagee.setImageResource(imageTutorial);
            nomeTutorial.setText(nameTutorial);
            avatarImage.setImageResource(avatarTutorial);
            description.setText(descriptionTutorial);
            authorTutorial.setText(author);
        }

        return view;
    }
}