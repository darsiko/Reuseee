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

import com.example.reuse.R;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private Context context;
    private List<User> userList;
    private ChatListAdapter.OnItemClickListener onItemClickListener;


    public ChatListAdapter(Context context, List<User> userList, ChatListAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false);
        return new ChatListAdapter.ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ChatListViewHolder holder, int position) {
        User user = userList.get(position);

        holder.userName.setText(user.getName());
        holder.userAvatar.setImageResource(user.getUserAvatarResId());

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
