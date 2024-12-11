package com.example.reuse.screens;

import android.os.Bundle;

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
import com.example.reuse.models.Product;
import com.example.reuse.models.User;

import java.util.ArrayList;
import java.util.List;

public class ChatListScreen extends Fragment implements ChatListAdapter.OnItemClickListener {
    private RecyclerView recyclerViewUsers;
    private ChatListAdapter adapter;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_chat_list_screen, container, false);

        // Initialize RecyclerViews after the view is inflated
        recyclerViewUsers = rootView.findViewById(R.id.chatList);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        userList = new ArrayList<>();
        userList.add(new User("Dario", "16/08/1977", "dario.sponchiado@gmail.com", "pass1234", "Italy", R.drawable.user));
        userList.add(new User("Marta", "16/08/1991", "marta.montalto@gmail.com", "pass1234", "Italy", R.drawable.user));
        // Add more products as needed...

        // Set the adapter for both RecyclerViews
        adapter = new ChatListAdapter(getContext(), userList, this);
        recyclerViewUsers.setAdapter(adapter);



        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onItemClick(User user) {
        // For example, navigate to the product details page or display a Toast
        Toast.makeText(getContext(), "Clicked on: " + user.getName(), Toast.LENGTH_SHORT).show();

        // You can now pass the product data to a new fragment or activity, e.g.
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getName());
        bundle.putInt("userAvatarResId", user.getUserAvatarResId());


        ChatScreen chatScreen = new ChatScreen();
        chatScreen.setArguments(bundle);

        // Perform the fragment transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chatScreen);
        transaction.addToBackStack(null);  // Add to back stack if you want to go back to this fragment
        transaction.commit();
    }
}