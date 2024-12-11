package com.example.reuse.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.reuse.R;
import com.example.reuse.adapter.ChatListAdapter;
import com.example.reuse.adapter.ChatMessageAdapter;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Message;
import com.example.reuse.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Fragment {
    private RecyclerView recyclerViewMessages;
    private ChatMessageAdapter adapter;
    private List<Message> messageList;

    TextView textView;
    LinearLayout goBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);
        recyclerViewMessages = view.findViewById(R.id.message_recycler_view);

        goBack = (LinearLayout) view.findViewById(R.id.goBackPreviusPage);
        textView = (android.widget.TextView) view.findViewById(R.id.chatUserName);

        Bundle args = getArguments();
        if (args != null) {
            String userName = args.getString("name", ""); // Default to empty if not found
            textView.setText("Chat con "+userName); // Set the username to the TextView
        }
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        messageList  = new ArrayList<>();
        messageList.add(new Message(System.currentTimeMillis(), "Hi how are you", R.drawable.user,true));
        messageList.add(new Message(System.currentTimeMillis(), "Fine thanks!", R.drawable.user,false));
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ChatMessageAdapter(getContext(), messageList);
        recyclerViewMessages.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}