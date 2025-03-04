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
import com.example.reuse.models.Scambio;
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
        textView = (TextView) view.findViewById(R.id.chatUserName);
        exchangeOption = view.findViewById(R.id.btnExchange);
        Bundle args = getArguments();

        String sellerUsername = args.getString("seller");

        textView.setText("Chat con "+sellerUsername); // Set the username to the TextView


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
                String[] sellerId = new String[1];
                sellerId[0] = args.getString("sellerID");
                String[] chatId = new String[1];
                //gather the current chat id
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
                dbRef.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null){
                        for(DataSnapshot d : task.getResult().getChildren()){
                            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            String u1 = d.child("idUtente1").getValue(String.class);
                            String u2 = d.child("idUtente2").getValue(String.class);
                            if(u1!=null && u2!=null && (u1.equals(userId) && u2.equals(sellerId[0])) || (u1.equals(sellerId[0]) && u2.equals(userId))){
                                chatId[0] = d.getKey();
                                List<Messaggio> mex = new ArrayList<>();
                                //gather messages list
                                for(DataSnapshot m : d.child("messaggi").getChildren()){
                                    //gather single message info
                                    String mexID = m.getKey();
                                    String content = m.child("contenuto").getValue(String.class);
                                    String data = m.child("dataeora").getValue(String.class);
                                    String mittente = m.child("idMittente").getValue(String.class);
                                    Messaggio temp = new Messaggio(mittente, data, false, content, mexID);
                                    mex.add(temp);
                                }
                                //retrieve Scambio
                                DatabaseReference tradeRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId[0]).child("scambio");
                                tradeRef.get().addOnSuccessListener(task2 -> {
                                    CurrentExchangeFragment exchangeFragmentScreen = new CurrentExchangeFragment();
                                    NewExchangeFragmentScreen newExchangeFragmentScreen = new NewExchangeFragmentScreen();
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();// Add to back stack if you want to go back to this fragment
                                    if(task2.getValue()!=null){
                                        //L'OFFERTA ESISTE GIà
                                        String idOff = task2.child("idOfferente").getValue(String.class);
                                        double cashOff = task2.child("soldiOfferente").getValue(Double.class);
                                        double cashRec = task2.child("soldiRicevente").getValue(Double.class);
                                        List<String> listaOffer = new ArrayList<>();
                                        for(DataSnapshot x : task2.child("listaOfferente").getChildren()){
                                            String offer = x.getKey();
                                            listaOffer.add(offer);
                                        }
                                        List<String> listaRec = new ArrayList<>();
                                        for(DataSnapshot x : task2.child("listaRicevente").getChildren()){
                                            String rec = x.getKey();
                                            listaRec.add(rec);
                                        }
                                        Bundle b = new Bundle();
                                        b.putString("sellerUsername", sellerUsername);
                                        b.putString("sellerId", sellerId[0]);
                                        b.putString("chatId", chatId[0]);
                                        exchangeFragmentScreen.setArguments(b);
                                        transaction.replace(R.id.fragment_container, exchangeFragmentScreen);
                                    }
                                    else{
                                        //L'OFFERTA NON ESISTE
                                        Bundle b = new Bundle();
                                        b.putString("sellerUsername", sellerUsername);
                                        b.putString("sellerId", sellerId[0]);
                                        b.putString("chatId", chatId[0]);
                                        b.putString("u1", u1);
                                        b.putString("u2", u2);
                                        newExchangeFragmentScreen.setArguments(b);
                                        transaction.replace(R.id.fragment_container, newExchangeFragmentScreen);
                                    }
                                    transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
                                    transaction.commit();
                                });
                            }
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