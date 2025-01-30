package com.example.reuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private Context context;
    private List<User> userList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public ChatListAdapter(Context context, List<User> userList, ChatListAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.userList.addAll(userList);
        this.onItemClickListener = onItemClickListener;
    }

    public void updateList(List<User> newUserList){
        this.userList.clear();
        this.userList.addAll(newUserList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        User user = userList.get(position);

        for(User u : userList){
            String profilePic = u.getImageUrl();
            if(profilePic!=null && !profilePic.isEmpty()){
                Glide.with(context)
                        .load(profilePic)
                        .into(holder.userAvatar);
            }
            else{
                holder.userAvatar
                        .setImageResource(R.drawable.user);
            }
            String userName = u.getUsername();
            if(userName!=null && !userName.isEmpty()){
                holder.userName.setText(userName);
            }
            else{
                holder.userName.setText("Username");
            }
        }
        //holder.userAvatar.setImageResource(user.getUserAvatarResId());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.contact_name);
            userAvatar = itemView.findViewById(R.id.profile_image);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}
