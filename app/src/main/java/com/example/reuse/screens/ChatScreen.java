package com.example.reuse.screens;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_screen, container, false);
        recyclerViewMessages = view.findViewById(R.id.message_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        goBack = (LinearLayout) view.findViewById(R.id.goBackPreviusPage);
        textView = (android.widget.TextView) view.findViewById(R.id.chatUserName);
        exchangeOption = view.findViewById(R.id.btnExchange);

        //trash = view.findViewById(R.id.trash);
        /*
        <LinearLayout
        android:id="@+id/trash"
        android:focusable="true"
        android:background="?android:attr/selectableItemBackground"
        android:clickable="true"

        android:layout_width="wrap_content"
        android:orientation="vertical"
        android:layout_height="wrap_content">

                <ImageView
        android:layout_width="wrap_content"
        android:layout_marginLeft="30dp"
        android:layout_height="wrap_content"
        android:src="@drawable/baseline_delete_24" />

                <TextView
        android:text="Elimina "
        android:layout_gravity="start"
        android:layout_marginLeft="20dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
            </LinearLayout>
         */
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
                            if(u1!=null && u2!=null && u1.equals(uID) && u2.equals(otherID)){
                                chatId[0] = s.getKey();
                                Chat c = new Chat(chatId[0]);
                                c.addMessaggio(uID, input);
                                messageList.clear();
                                loadChat();
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
        loadChat();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String sellerName = args.getString("seller");
        textView.setText("Chat con " + sellerName);

        /*
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> myChats = new ArrayList<>();

                String s = args.getString("size");
                int size = Integer.parseInt(s);

                for(int i=0; i<size; ++i){
                    myChats.add(args.getString(Integer.toString(i)));
                }

                String[] sellerID = new String[1];
                for(String ids : myChats){
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(ids);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String username = snapshot.child("username").getValue(String.class);
                                if(username.equals(sellerName)){
                                    sellerID[0] = ids;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }


                String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                DatabaseReference chatUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uID).child("chats");
                Chat[] c = new Chat[1];
                chatUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot temp : snapshot.getChildren()){
                                //chat1 Williams
                                //chat2 Pippo
                                String chatID = temp.getValue(String.class);
                                if(chatID!=null && !chatID.isEmpty()){
                                    DatabaseReference u1 = FirebaseDatabase.getInstance().getReference("Chats").child(chatID).child("idUtente1");
                                    u1.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                if(snapshot.getValue(String.class).equals(uID)){
                                                    DatabaseReference u2 = FirebaseDatabase.getInstance().getReference("Chats").child(chatID).child("idUtente2");
                                                    u2.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshott) {
                                                            if(snapshott.exists()){
                                                                if(snapshott.getValue(String.class).equals(sellerID[0])){
                                                                    //QUESTA è LA CHAT DA ELIMINARE
                                                                    Chat temp = new Chat(chatID);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {}
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {}
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                getParentFragmentManager().popBackStack();
            }
        });
         */
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
                                        if(!contains){
                                            Messaggio m = new Messaggio(u1, data, false, content);
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

    private void attachChatListener(String chatId){

        /*
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat c = new Chat(chatId);
                messageList.clear();
                messageList.addAll(c.getMessaggi());
                adapter.updateList(messageList);
                adapter.notifyDataSetChanged(); // Aggiorna la RecyclerView
                recyclerViewMessages.scrollToPosition(messageList.size() - 1); // Scorri alla fine
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
         */
    }

    //?????
    private void loadChats() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Messaggio message = snapshot.getValue(Messaggio.class);
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