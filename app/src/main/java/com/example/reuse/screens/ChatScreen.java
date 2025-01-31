package com.example.reuse.screens;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.ChatListAdapter;
import com.example.reuse.adapter.ChatMessageAdapter;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.models.Chat;
import com.example.reuse.models.Messaggio;
import com.example.reuse.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChatScreen extends Fragment {
    private RecyclerView recyclerViewMessages;
    private ChatMessageAdapter adapter;
    private List<Messaggio> messageList;
    private DatabaseReference databaseReference;
    private List<Messaggio> chatList = new ArrayList<>();
    TextView textView;
    EditText chatInput;
    ImageView send;
    LinearLayout exchangeOption;
    LinearLayout goBack; //LinearLayout trash;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);
        recyclerViewMessages = view.findViewById(R.id.message_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        goBack = (LinearLayout) view.findViewById(R.id.goBackPreviusPage);
        textView = (android.widget.TextView) view.findViewById(R.id.chatUserName);
        exchangeOption = view.findViewById(R.id.btnExchange);
        Bundle args = getArguments();

        String sellerId = args.getString("sellerId");

        textView.setText("Chat con "+sellerId); // Set the username to the TextView


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

        exchangeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                b.putString("sellerId", sellerId);
                Chat c = new Chat(sellerId, FirebaseAuth.getInstance().getUid());
                b.putString("chatId", c.getId());
                c.uploadChat();
                c.checkOfferta(new Chat.CheckOffertaCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        System.out.println(exists);
                        CurrentExchangeFragment exchangeFragmentScreen = new CurrentExchangeFragment();
                        NewExchangeFragmentScreen newExchangeFragmentScreen = new NewExchangeFragmentScreen();
                        if(exists){
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            exchangeFragmentScreen.setArguments(b);
                            transaction.replace(R.id.fragment_container, exchangeFragmentScreen);
                            transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                            transaction.commit();
                        }else{
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            newExchangeFragmentScreen.setArguments(b);
                            transaction.replace(R.id.fragment_container, newExchangeFragmentScreen);
                            transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                            transaction.commit();
                        }
                    }
                });

            }
        });


        send = view.findViewById(R.id.send_button);
        chatInput = view.findViewById(R.id.chat_input);

        send.setOnClickListener(v -> {
            String input = chatInput.getText().toString();
            if(!input.isEmpty()){
                chatInput.setText("");
                String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                String otherID = args.getString("sellerID");

                String[] chatId = new String[1];

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
                dbRef.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for(DataSnapshot s : task.getResult().getChildren()){
                            String u1 = s.child("idUtente1").getValue(String.class);
                            String u2 = s.child("idUtente2").getValue(String.class);
                            if(u1!=null && u2!=null && (u1.equals(uID) && u2.equals(otherID)) || (u1.equals(otherID) && u2.equals(uID))){
                                chatId[0] = s.getKey();
                                Chat c = new Chat(chatId[0]);
                                c.addMessaggio(uID, input);
                            }
                        }
                    }
                });
            }
        });

        // Inflate the layout for this fragment

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnable = new Runnable(){
            @Override
            public void run(){
                messageList.clear();
                loadChat();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String sellerName = args.getString("seller");
        textView.setText("Chat con " + sellerName);
    }


    private void loadChat(){
        String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String otherID = getArguments().getString("sellerID");

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");

        dbRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot s : task.getResult().getChildren()) {
                    String u1 = s.child("idUtente1").getValue(String.class);
                    String u2 = s.child("idUtente2").getValue(String.class);
                    if (u1 != null && u2 != null && ((u1.equals(uID) && u2.equals(otherID)) || (u1.equals(otherID) && u2.equals(uID)))){
                        String chatId = s.getKey();
                        //attachChatListener(chatId);
                        if(chatId!=null){
                            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId).child("messaggi");
                            chatRef.get().addOnCompleteListener(task2 -> {
                                if(task2.isSuccessful() && task2.getResult()!=null){
                                    for(DataSnapshot d : task2.getResult().getChildren()){
                                        String mexID = d.getKey();
                                        boolean contains = false;
                                        for(Messaggio m : messageList){
                                            if(m.getId().equals(mexID)){
                                                contains = true;
                                                break;
                                            }
                                        }
                                        String data = d.child("dataora").getValue(String.class);
                                        String content = d.child("contenuto").getValue(String.class);
                                        String mittente = d.child("idMittente").getValue(String.class);
                                        if(!contains){
                                            Messaggio m = new Messaggio(mittente, data, false, content);
                                            messageList.add(m);
                                        }
                                    }
                                    adapter.updateList(messageList);
                                    adapter.notifyDataSetChanged();
                                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                                }
                            });
                        }
                        return; // Una volta trovata la chat, non serve continuare
                    }
                }
            }
        });
    }
}