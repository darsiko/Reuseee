package com.example.reuse.screens;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.reuse.R;
import com.example.reuse.adapter.AdapterTutorial;
import com.example.reuse.models.Tutorial;

import java.util.List;

public class SearchResultFragmentTutorial extends Fragment {
    private RecyclerView recyclerViewResults;
    private AdapterTutorial adapter;
    private List<Tutorial> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Recupera i risultati della ricerca dal Bundle
        if (getArguments() != null) {
            filteredList = getArguments().getParcelableArrayList("filteredProducts");
        }

        // Configura l'adapter con i risultati filtrati
        adapter = new AdapterTutorial(getContext(), filteredList, tutorial -> {
            // Implementa l'azione di clic sugli elementi se necessario
        });
        recyclerViewResults.setAdapter(adapter);

        return view;
    }
}
