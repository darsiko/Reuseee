package com.example.reuse.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Product;

import java.util.List;

public class SearchResultFragment extends Fragment {
    private RecyclerView recyclerViewResults;
    private ProductAdapter adapter;
    private List<Product> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.reuse.R.layout.fragment_search_result, container, false);

        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Recupera i risultati della ricerca dal Bundle
        if (getArguments() != null) {
            filteredList = getArguments().getParcelableArrayList("filteredProducts");
            Log.d("SearchResultFragment", "Filtered products: " + filteredList);
        }

        // Configura l'adapter con i risultati filtrati
        if (filteredList != null && !filteredList.isEmpty()) {
            adapter = new ProductAdapter(getContext(), filteredList, tutorial -> {
                // Implementa l'azione di clic sugli elementi se necessario
            });
            recyclerViewResults.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), "Nessun risultato trovato.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
