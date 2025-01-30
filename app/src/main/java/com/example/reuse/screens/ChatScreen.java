package com.example.reuse.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.ChatListAdapter;
import com.example.reuse.adapter.ChatMessageAdapter;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Chat;
import com.example.reuse.models.Message;
import com.example.reuse.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatScreen extends Fragment {
    private RecyclerView recyclerViewMessages;
    private ChatMessageAdapter adapter;
    private List<Message> messageList;
    private DatabaseReference databaseReference;
    private List<Message> chatList = new ArrayList<>();
    TextView textView;
    LinearLayout goBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);
        recyclerViewMessages = view.findViewById(R.id.message_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
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

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new ChatMessageAdapter(getContext(), messageList);
        recyclerViewMessages.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
    private void loadChats() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                       // Set product ID from Firebase key
                        chatList.add(message);
                    }
                }
                adapter.updateList(chatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductList", "Error fetching data: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}