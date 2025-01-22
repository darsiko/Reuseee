package com.example.reuse.screens;

import static java.util.Locale.filter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        tutorialList.add(new Tutorial(R.drawable.shoes, "Sneakers", R.drawable.user, "Come è semplice riparare le sneakers", "Topo Gigio", "https://www.youtube.com/watch?v=iAPVWEixnWQ" ));
        tutorialList.add(new Tutorial(R.drawable.caldaia, "Riparare la caldaia", R.drawable.user, "Come è semplice riparare la caldaia", "Topo Gigetto", "https://www.youtube.com/watch?v=cpzytyuSbNI" ));
        tutorialList.add(new Tutorial(R.drawable.margherita, "Riparare la lavatrice", R.drawable.user, "Come è semplice riparare la lavatrice", "Topo Gigetto", "https://www.youtube.com/watch?v=brs9OTU9Yh0" ));
        tutorialList.add(new Tutorial(R.drawable.telefono, "Riparare lo schermo dell'Iphone 11", R.drawable.user, "Come è semplice riparare lo schermo dell'iphone 11", "Topo Gigetto", "https://www.youtube.com/watch?v=CMkgypeOa-4"));
        // Set the adapter for both RecyclerViews
        adapter = new AdapterTutorial(getContext(), tutorialList, this);
        recyclerViewTutorial.setAdapter(adapter);
        // Inflate the layout for this fragment
        EditText searchBar = view.findViewById(R.id.editTextText);
        searchBar.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

        });
        return view;
    }
    private void filter(String text) {
        List<Tutorial> filteredList = new ArrayList<>();

        for (Tutorial tutorial : tutorialList) {
            if (tutorial.getName().toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(tutorial);
            }
        }

        adapter.updateList(filteredList);
    }
    @Override
    public void onItemClick(Tutorial tutorial) {
        Toast.makeText(getContext(), "Clicked on: " + tutorial.getName(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("nameTutorial", tutorial.getName());
        bundle.putInt("imageTutorial", tutorial.getImageResId());
        bundle.putString("author", tutorial.getAuthor());
        bundle.putString("descriptionTutorial", tutorial.getDescription());
        bundle.putInt("avatarTutorial", tutorial.getUserAvatarResId()); // For ImageView
        // Add other product details to the bundle
        bundle.putString("url", tutorial.getUrlTutorial());
        TutorialScreen tutorialScreen = new TutorialScreen();
        tutorialScreen.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, tutorialScreen);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}