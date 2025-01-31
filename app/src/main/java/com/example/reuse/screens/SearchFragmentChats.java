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
import com.example.reuse.adapter.ChatListAdapter;
import com.example.reuse.models.Tutorial;
import com.example.reuse.models.User;

import java.util.List;


public class SearchFragmentChats extends Fragment {
    private RecyclerView recyclerViewResults;
    private ChatListAdapter adapter;
    private List<User> filteredList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Recupera i risultati della ricerca dal Bundle
        if (getArguments() != null) {
            filteredList = getArguments().getParcelableArrayList("filteredChatUsers");
        }

        // Configura l'adapter con i risultati filtrati
        adapter = new ChatListAdapter(getContext(), filteredList, tutorial -> {
            // Implementa l'azione di clic sugli elementi se necessario
        });
        recyclerViewResults.setAdapter(adapter);

        return view;
    }
}