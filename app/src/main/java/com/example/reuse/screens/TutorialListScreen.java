package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.AdapterTutorial;
import com.example.reuse.models.Tutorial;

import java.util.ArrayList;
import java.util.List;


public class TutorialListScreen extends Fragment implements AdapterTutorial.OnItemClickListener {
    private RecyclerView recyclerViewTutorial;
    private AdapterTutorial adapter;
    private List<Tutorial> tutorialList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_list_screen, container, false);
        recyclerViewTutorial = view.findViewById(R.id.recycleViewTutorial);
        recyclerViewTutorial.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        tutorialList = new ArrayList<>();
        tutorialList.add(new Tutorial(R.drawable.shoes, "Sneakers Verdi", R.drawable.user, "Come è semplice riparare le sneakers", "Topo Gigio" ));
        tutorialList.add(new Tutorial(R.drawable.shoes, "Sneakers Verdi", R.drawable.user, "Come è semplice riparare le sneakers", "Topo Gigetto" ));
        // Set the adapter for both RecyclerViews
        adapter = new AdapterTutorial(getContext(), tutorialList, this);
        recyclerViewTutorial.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(Tutorial tutorial) {
        Toast.makeText(getContext(), "Clicked on: " + tutorial.getName(), Toast.LENGTH_SHORT).show();

        // You can now pass the product data to a new fragment or activity, e.g.
        Bundle bundle = new Bundle();
        bundle.putString("nameTutorial", tutorial.getName());
        bundle.putInt("imageTutorial", tutorial.getImageResId());
        bundle.putString("author", tutorial.getAuthor());
        bundle.putString("descriptionTutorial", tutorial.getDescription());
        bundle.putInt("avatarTutorial", tutorial.getUserAvatarResId()); // For ImageView
        // Add other product details to the bundle

        TutorialScreen tutorialScreen = new TutorialScreen();
        tutorialScreen.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, tutorialScreen);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}