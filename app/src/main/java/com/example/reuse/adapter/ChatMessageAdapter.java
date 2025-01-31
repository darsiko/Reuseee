package com.example.reuse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reuse.R;
import com.example.reuse.models.Messaggio;
import com.example.reuse.models.Product;
import com.example.reuse.models.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<Messaggio> messageList = new ArrayList<>();

    public ChatMessageAdapter(Context context, List<Messaggio> messageList) {
        this.context = context;
        this.messageList.addAll(messageList);
    }

    @Override
    public int getItemViewType(int position) {
        Messaggio message = messageList.get(position);
        String cID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if(message.getIdMittente().equals(cID)){
            //sono il mittente
            return 1;
        }
        else return 2;
        //return message.isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //IL PROBLEMA è QUI
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_item_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_item_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messaggio message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void updateList(List<Messaggio> chatList) {
        this.messageList.clear();
        this.messageList.addAll(chatList);
        notifyDataSetChanged();
    }

    // ViewHolder for sent messages
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageContent, messageTimestamp;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageContent = itemView.findViewById(R.id.sent_message_text);
            messageTimestamp = itemView.findViewById(R.id.sent_message_timestamp);
        }

        void bind(Messaggio message) {
            messageContent.setText(message.getContenuto());
            messageTimestamp.setText(message.getDataora());
        }
    }

    // ViewHolder for received messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageContent, messageTimestamp;
        ImageView userAvatarResId;
        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            userAvatarResId = itemView.findViewById(R.id.received_message_avatar);
            messageContent = itemView.findViewById(R.id.received_message_text);
            messageTimestamp = itemView.findViewById(R.id.received_message_timestamp);
        }

        void bind(Messaggio message) {
            messageContent.setText(message.getContenuto());
            messageTimestamp.setText(message.getDataora());
            //userAvatarResId.setImageResource(message.getUserAvatarResId());

        }
    }

    // Helper to format timestamps
    private static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(timestamp);
    }
}
