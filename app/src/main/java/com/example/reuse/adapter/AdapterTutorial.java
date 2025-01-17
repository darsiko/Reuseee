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
import com.example.reuse.models.Tutorial;
import com.example.reuse.models.User;

import java.util.List;

public class AdapterTutorial extends RecyclerView.Adapter<AdapterTutorial.ViewHolderTutorial> {
    private Context context;
    private List<Tutorial> tutorialList;
    private AdapterTutorial.OnItemClickListener onItemClickListener;

    public AdapterTutorial(Context context, List<Tutorial> tutorialList, AdapterTutorial.OnItemClickListener onItemClickListener){
        this.context = context;
        this.tutorialList = tutorialList;
        this.onItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolderTutorial onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tutorial, parent, false);
        return new AdapterTutorial.ViewHolderTutorial(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTutorial holder, int position) {
        Tutorial tutorial = tutorialList.get(position);

        holder.name.setText(tutorial.getName());
        holder.userAvatar.setImageResource(tutorial.getUserAvatarResId());
        holder.author.setText(tutorial.getAuthor());
        holder.resImageId.setImageResource(tutorial.getImageResId());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(tutorial));
    }
    public void updateList(List<Tutorial> newTutorialList) {
        tutorialList = newTutorialList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return tutorialList.size();
    }

    public static class ViewHolderTutorial extends RecyclerView.ViewHolder {

        ImageView userAvatar, resImageId;
        TextView name, author;

        public ViewHolderTutorial(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tutorialName);
            resImageId = itemView.findViewById(R.id.tutorialImage);
            userAvatar = itemView.findViewById(R.id.tutorialAuthor);
            author = itemView.findViewById(R.id.sellerName);

        }

    }
    public interface OnItemClickListener {
        void onItemClick(Tutorial tutorial);
    }
}
