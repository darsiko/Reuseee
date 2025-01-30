package com.example.reuse.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reuse.R;
import com.example.reuse.adapter.ChatListAdapter;
import com.example.reuse.adapter.ProductAdapter;
import com.example.reuse.adapter.ProductBroughtAdapter;
import com.example.reuse.models.Chat;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatListScreen extends Fragment implements ChatListAdapter.OnItemClickListener {
    private RecyclerView recyclerViewUsers;
    private ChatListAdapter adapter;
    private List<User> userList;
    String sellerId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat_list_screen, container, false);

        // Initialize RecyclerViews after the view is inflated
        recyclerViewUsers = rootView.findViewById(R.id.chatList);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        userList = new ArrayList<>();
        adapter = new ChatListAdapter(getContext(), userList, this);
        recyclerViewUsers.setAdapter(adapter);

        Bundle bundle = getArguments();
        if(bundle!=null){
            sellerId = bundle.getString("sellerId");
            loadUsers(sellerId);
        }
        return rootView;
    }

    private boolean contains(String userName){
        for(User u : userList){
            if(u.getUsername()==userName) return true;
        }
        return false;
    }


    //chat con te stesso
    private void loadUsers(final String uID){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uID);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username=snapshot.child("username").getValue(String.class);
                    if(!contains(username)){
                        String nome=snapshot.child("nome").getValue(String.class);
                        String cognome=snapshot.child("cognome").getValue(String.class);
                        String telefono=snapshot.child("telefono").getValue(String.class);
                        String indirizzo = snapshot.child("indirizzo").getValue(String.class);
                        String data = snapshot.child("data").getValue(String.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        String stato = snapshot.child("stato").getValue(String.class);
                        String citta = snapshot.child("citta").getValue(String.class);

                        int cap = snapshot.child("cap").getValue(int.class);
                        List<String> productsForSale = new ArrayList<>();
                        for (DataSnapshot productSnapshot : snapshot.child("productsForSale").getChildren()) {
                            String pid = productSnapshot.getValue(String.class);
                            if (pid != null) {
                                productsForSale.add(pid);
                            }
                        }
                        List<String> chats = new ArrayList<>();
                        for (DataSnapshot chatSnapshot : snapshot.child("chats").getChildren()) {
                            String cid = chatSnapshot.getValue(String.class);
                            if (cid != null) {
                                chats.add(cid);
                            }
                        }
                        User user = new User(username, nome, cognome, telefono, stato, citta, cap, indirizzo, data, imageUrl, productsForSale, chats);
                        userList.add(user);
                        adapter.updateList(userList);
                    }
                    //String username, String nome, String cognome, String telefono, String stato,
                    // String citta, int cap, String indirizzo, String data, String imageUrl, List<String> productsForSale

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    @Override
    public void onItemClick(User user) {
        // For example, navigate to the product details page or display a Toast
        Toast.makeText(getContext(), "Clicked on: " + user.getUsername(), Toast.LENGTH_SHORT).show();


        // You can now pass the product data to a new fragment or activity, e.g.
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getUsername());

        //modifica momentanea bundle.putInt("userAvatarResId", user.getUserAvatarResId());
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Chat chat = new Chat(currentUserId, sellerId);
        chat.uploadChat();
        ChatScreen chatScreen = new ChatScreen();
        chatScreen.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chatScreen);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}